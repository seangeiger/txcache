<?php

  // XXX Report undefined variables
  //error_reporting(E_ALL);

if (isset($argv) && $argv)
  parse_str($argv[1], $HTTP_POST_VARS);

include("txcache-config.php");

$postgres = true;

if ($postgres) {
  if (TXCACHE) {
    function sql_close($link)
    {
      global $TX, $DB;
      if ($link != $TX)
        die("$link isn't txcache link $TX");
      if ($DB)
        pg_close($DB);
    }
  } else {
    function sql_close($link)
    {
      pg_close($link);
    }
  }

  function sql_error()
  {
    return pg_last_error();
  }

  function sql_fetch_array($res)
  {
    return pg_fetch_array($res);
  }

  function sql_free_result($res)
  {
    return pg_free_result($res);
  }

  function sql_num_rows($res)
  {
    return pg_num_rows($res);
  }

  function sql_query($sql, $link)
  {
    # Stupid, stupid PHP
    get_magic_quotes_gpc() or die("Depends on magic quotes");
    $sql = str_replace(array("\\'", "\""), array("''", "'"), $sql);
    $link or die("No link given");
    if (TXCACHE) {
      global $TX;
      return txcache_query($TX, $sql);
    } else
      return pg_query($link, $sql);
  }

  $ID_DEFAULT = "DEFAULT";
} else {
  function sql_close($link)
  {
    mysql_close($link);
  }

  function sql_error($link)
  {
    return mysql_error($link);
  }

  function sql_fetch_array($res)
  {
    return mysql_fetch_array($res);
  }

  function sql_free_result($res)
  {
    return mysql_free_result($res);
  }

  function sql_num_rows($res)
  {
    return mysql_num_rows($res);
  }

  function sql_query($sql, $link=NULL)
  {
    if ($link != NULL)
      return mysql_query($sql, $link);
    else
      return mysql_query($sql);
  }

  $ID_DEFAULT = "NULL";
}
 
function getDatabaseLink(&$link)
{
  global $postgres;

  if ($postgres) {
    if (TXCACHE) {
      global $TX, $DB;
      $TX = txcache_pconnect('getDatabaseLinkLazy', TXCACHEBYPASS) or die("ERROR: Could not connect to cache");
      $DB = false;
      $link = $TX;
    } else {
      $link = getDatabaseLinkLazy();
    }
  } else {
    $link = mysql_pconnect("localhost", "cecchet", "") or die ("ERROR: Could not connect to database");
    mysql_select_db("rubis", $link) or die("ERROR: Couldn't select RUBiS database");
  }
}

function getDatabaseLinkLazy()
{
  global $DB;
  $DB = pg_pconnect(DBCONNSTRING) or die("ERROR: Could not connect to database");
  return $DB;
}

function getMicroTime()
{
  list($usec, $sec) = explode(" ", microtime());
  return ((float)$usec + (float)$sec);
}

function printHTMLheader($title)
{
  include("header.html");
  print("<title>$title</title>");
}

function printHTMLHighlighted($msg)
{
  print("<TABLE width=\"100%\" bgcolor=\"#CCCCFF\">\n");
  print("<TR><TD align=\"center\" width=\"100%\"><FONT size=\"4\" color=\"#000000\"><B>$msg</B></FONT></TD></TR>\n");
  print("</TABLE><p>\n");
}

function printHTMLfooter($scriptName, $startTime)
{
  $endTime = getMicroTime();
  $totalTime = $endTime - $startTime;
  printf("<br><hr>RUBiS (C) Rice University/INRIA<br><i>Page generated by $scriptName in %.3f seconds.</i><br>\n", $totalTime);
  printf("<i>Virtual time is %s.</i><br>\n", virtualTimeSQL());
  print("</body>\n");
  print("</html>\n");	
}

function printError($scriptName, $startTime, $title, $error)
{
  printHTMLheader("RUBiS ERROR: $title");
  print("<h2>We cannot process your request due to the following error :</h2><br>\n");
  print($error);
  printHTMLfooter($scriptName, $startTime);      
}

function authenticateImpl($link, $nickname, $password)
{
  txcache_invaltag("users", "nickname", $nickname);
  $result = sql_query("SELECT id FROM users WHERE nickname=\"$nickname\" AND password=\"$password\"", $link) or die("ERROR: Authentification query failed");
  if (sql_num_rows($result) == 0)
    return -1;
  $row = sql_fetch_array($result);
  return $row["id"];
}

function authenticate($nickname, $password, $link)
{
  return wrap(true, 'authenticateImpl', $link, $nickname, $password);
}

$runningTransaction = null;
$transactionIsReadOnly = false;

function beginRO($link)
{
  global $runningTransaction, $transactionIsReadOnly, $postgres, $TX;
  if ($runningTransaction)
    die("ERROR: runningTransaction is non-null");
  if (TXCACHE) {
    if (RANDOMIZE_FRESHNESS) 
      $freshness = rand(FRESHNESS*RANDOMIZE_FRESHNESS_FACTOR, FRESHNESS);
    else
      $freshness = FRESHNESS;
    txcache_begin_ro($TX, $freshness) or die("ERROR: Failed to beginRO");
  }
  elseif ($postgres)
    sql_query("BEGIN READ ONLY", $link);
  else
    sql_query("BEGIN", $link);
  register_shutdown_function('maybe_rollback');
  $runningTransaction = $link;
  $transactionIsReadOnly = true;
}

function beginRW($link)
{
  global $runningTransaction, $transactionIsReadOnly;
  if ($runningTransaction)
    die("ERROR: runningTransaction is non-null");
  sql_query("BEGIN", $link);
  register_shutdown_function('maybe_rollback');
  $runningTransaction = $link;
  $transactionIsReadOnly = false;
}

function commit($link)
{
  global $runningTransaction, $transactionIsReadOnly, $TX;
  if ($transactionIsReadOnly && TXCACHE)
    txcache_commit($TX) or die("ERROR: Failed to commit");
  else
    sql_query("COMMIT", $link);
  $runningTransaction = null;
}

function rollback($link)
{
  global $runningTransaction, $transactionIsReadOnly, $TX;
  if ($transactionIsReadOnly && TXCACHE)
    txcache_commit($TX) or die("ERROR: Failed to commit");
  else
    sql_query("ROLLBACK", $link);
  $runningTransaction = null;
}

function maybe_rollback()
{
  global $runningTransaction;
  if ($runningTransaction) {
    echo "<h2>Rolling back from abort</h2><br>\n";
    rollback($runningTransaction);
  }
}

if (TXCACHE) {
  function txcache_inval($table, $index, $value)
  {
    global $TX;
    if ($index == "*") {
      $tag = "$table:";
    } else {
      $tag = "$table:$index=$value:";
    }
    if (TXCACHEINVALIDATIONS) {
        txcache_explicitly_invalidate($TX, $tag);        
    }
  }
  function txcache_invaltag($table, $index, $value)
  {
    global $TX;
    if ($index == "*") {
      $tag = "$table:";
    } else {
      $tag = "$table:$index=$value:";
    }
    if (TXCACHEINVALIDATIONS) {
        txcache_add_explicit_invalidation_tag($TX, $tag);
    }
  }
  function wrap()
  {
    $args = func_get_args();
//    if (!TXCACHEINVALIDATIONS)
//        $args[0] = false;
    array_unshift($args, $args[2]);
    return call_user_func_array('txcache_wrap', $args);
  }
 } else {
  function txcache_inval($table, $index, $value)
  {
  }
  function txcache_invaltag($table, $index, $value)
  {
  }
  function wrap()
  {
    $args = func_get_args();
    array_shift($args);
    return call_user_func_array('call_user_func', $args);
  }
 }

if (MICROCACHE) {
  function getUserImpl($link, $userId)
  {
    txcache_invaltag("users", "id", $userId);
    $res = sql_query("SELECT * FROM users WHERE users.id=$userId", $link) or die("ERROR: Query for user $userId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }
  function getUser($link, $userId)
  {
    global $TX;
    return txcache_wrap($TX, true, 'getUserImpl', $link, $userId);
  }

  function getItemImpl($link, $itemId)
  {
    txcache_invaltag("items", "id", $itemId);
    $res = sql_query("SELECT * FROM items WHERE id=$itemId", $link) or die("ERROR: Query for item $itemId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }
  function getItem($link, $itemId)
  {
    global $TX;
    return txcache_wrap($TX, true, 'getItemImpl', $link, $itemId);
  }

  function getOldItemImpl($link, $itemId)
  {
    txcache_invaltag("old_items", "id", $itemId);
    $res = sql_query("SELECT * FROM old_items WHERE id=$itemId", $link) or die("ERROR: Query for old item $itemId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }
  function getOldItem($link, $itemId)
  {
    global $TX;
    return txcache_wrap($TX, true, 'getOldItemImpl', $link, $itemId);
  }

  function getAnyItemImpl($link, $itemId)
  {
    $row = getItem($link, $itemId);
    if ($row)
      return $row;

    $row = getOldItemImpl($link, $itemId);
    return $row;
  }
  function getAnyItem($link, $itemId)
  {
    global $TX;
    return txcache_wrap($TX, true, 'getAnyItemImpl', $link, $itemId);
  }

  
} else {

    
  function getUser($link, $userId)
  {
    $res = sql_query("SELECT * FROM users WHERE users.id=$userId", $link) or die("ERROR: Query for user $userId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }

  function getItem($link, $itemId)
  {
    $res = sql_query("SELECT * FROM items WHERE id=$itemId", $link) or die("ERROR: Query for item $itemId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }

  function getAnyItem($link, $itemId)
  {
    $row = getItem($link, $itemId);
    if ($row)
      return $row;

    $res = sql_query("SELECT * FROM old_items WHERE id=$itemId", $link) or die("ERROR: Query for old item $itemId failed");
    if (sql_num_rows($res) == 0)
      return NULL;
    $row = pg_fetch_assoc($res);
    sql_free_result($res);
    return $row;
  }

  function getOldItem($link, $itemId)
  {
      $res = sql_query("SELECT * FROM old_items WHERE id=$itemId", $link) or die("ERROR: Query for old item $itemId failed");
      if (sql_num_rows($res) == 0)
          return NULL;
      $row = pg_fetch_assoc($res);
      sql_free_result($res);
      return $row;
  }
}

function printCommentTableImpl($link, $userId)
{
       // Get the comments about the user
    txcache_invaltag("comments", "to_user", $userId);
    $commentsResult = sql_query("SELECT * FROM comments WHERE comments.to_user_id=$userId", $link) or die("ERROR: Query failed for the list of comments.");
    if (sql_num_rows($commentsResult) == 0)
      print("<h2>There is no comment for this user.</h2><br>\n");
    else
    {
	print("<DL>\n");
	while ($commentsRow = sql_fetch_array($commentsResult))
	{
	    $authorId = $commentsRow["from_user_id"];
            txcache_invaltag("users", "id", $authorId);
            $authorRow = getUser($link, $authorId);
            $authorName = $authorRow["nickname"];
	    $date = $commentsRow["date"];
	    $comment = $commentsRow["comment"];
	    print("<DT><b><BIG><a href=\"/PHP/ViewUserInfo.php?userId=".$authorId."\">$authorName</a></BIG></b>"." wrote the ".$date."<DD><i>".$comment."</i><p>\n");
	}
	print("</DL>\n");

    }
    sql_free_result($commentsResult);
}

function printCommentTable($link, $userId)
{
    global $TX;
    return txcache_wrap($TX, true, 'printCommentTableImpl', $link, $userId);
}


if (VIRTUALIZE_TIME) {
    function virtualTime()
    {
        return time() - REAL_TIME_BASE + VIRTUAL_TIME_BASE;
    }    
} else {
    function virtualTime()
    {
        return time();
    }
}

function virtualTimeSQL()
{
    return date("Y-m-d H:i:s", virtualTime());
}

?>