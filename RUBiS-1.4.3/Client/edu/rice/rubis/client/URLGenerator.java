/* -*- c-basic-offset: 2 -*-
 * RUBiS
 * Copyright (C) 2002, 2003, 2004 French National Institute For Research In Computer
 * Science And Control (INRIA).
 * Contact: jmob@objectweb.org
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or any later
 * version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 * Initial developer(s): Emmanuel Cecchet, Julie Marguerite
 * Contributor(s): 
 */
 package edu.rice.rubis.client;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.Random;

/**
 * This abstract class provides the needed URLs to access all features of RUBiS.
 * Only the function returning script names must be defined in subclasses else
 * every URL is generated by this class.
 *
 * @author <a href="mailto:cecchet@rice.edu">Emmanuel Cecchet</a> and <a href="mailto:julie.marguerite@inrialpes.fr">Julie Marguerite</a>
 * @version 1.0
 */

public abstract class URLGenerator
{
  private static final String protocol = "http";
  private Vector<URL> webSites;
  private String HTMLPath;
  private String scriptPath;
  private String extraQueryString;

  /**
   * Returns the name of the About Me script according to the implementation (PHP, EJB or Servlets).
   *
   * @return About Me script name
   */
  public abstract String AboutMeScript();

  /**
   * Returns the name of the Browse Categories script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Browse Categories script name
   */
  public abstract String BrowseCategoriesScript();

  /**
   * Returns the name of the Browse Regions script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Browse Regions script name
   */
  public abstract String BrowseRegionsScript();

  /**
   * Returns the name of the Buy Now script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Buy Now script name
   */
  public abstract String BuyNowScript();

  /**
   * Returns the name of the Buy Now Auth script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Buy Now Auth script name
   */
  public abstract String BuyNowAuthScript();

  /**
   * Returns the name of the Put Bid script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Put Bid script name
   */
  public abstract String PutBidScript();

  /**
   * Returns the name of the Put Bid Auth script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Put Bid Auth script name
   */
  public abstract String PutBidAuthScript();

  /**
   * Returns the name of the Put Comment script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Put Comment script name
   */
  public abstract String PutCommentScript();

  /**
   * Returns the name of the Put Comment Auth script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Put Comment Auth script name
   */
  public abstract String PutCommentAuthScript();

  /**
   * Returns the name of the Register Item script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Register Item script name
   */
  public abstract String RegisterItemScript();

  /**
   * Returns the name of the Register User script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Register User script name
   */
  public abstract String RegisterUserScript();

  /**
   * Returns the name of the Search Items By Category script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Search Items By Category script name
   */
  public abstract String SearchItemsByCategoryScript();

  /**
   * Returns the name of the Search Items By Region script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Search Items By Region script name
   */
  public abstract String SearchItemsByRegionScript();

  /**
   * Returns the name of the Sell Item Form script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Sell Item Form script name
   */
  public abstract String SellItemFormScript();


  /**
   * Returns the name of the Store Buy Now script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Store Buy Now script name
   */
  public abstract String StoreBuyNowScript();

  /**
   * Returns the name of the Store Bid script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Store Bid script name
   */
  public abstract String StoreBidScript();

  /**
   * Returns the name of the Store Comment script according to the implementation (PHP, EJB or Servlets).
   *
   * @return Store Comment script name
   */
  public abstract String StoreCommentScript();

  /**
   * Returns the name of the View Bid History script according to the implementation (PHP, EJB or Servlets).
   *
   * @return View Bid History script name
   */
  public abstract String ViewBidHistoryScript();

  /**
   * Returns the name of the View Item script according to the implementation (PHP, EJB or Servlets).
   *
   * @return View Item script name
   */
  public abstract String ViewItemScript();

  /**
   * Returns the name of the View User Info script according to the implementation (PHP, EJB or Servlets).
   *
   * @return View User Info script name
   */
  public abstract String ViewUserInfoScript();


  /**
   * Set the name and port of the Web site running RUBiS as well as the
   * directories where the HTML and scripts reside.
   *
   * @param host Web site address
   * @param port HTTP server port
   * @param HTMLFilesPath path where HTML files reside
   * @param ScriptFilesPath path to the script files
   */
  public URLGenerator(Vector<URL> hosts, String HTMLFilesPath, String ScriptFilesPath, String ExtraQueryString)
  {
    webSites    = hosts;
    HTMLPath    = HTMLFilesPath;
    scriptPath  = ScriptFilesPath;
    extraQueryString = ExtraQueryString;
  }

  /** Set the web site name.
   * For example: 
   * <pre>
   * URLGenerator urlGen = new URLGenerator();
   * urlGen.setWebSiteName("www.testbed.cs.rice.edu");
   * </pre>
   *
   * @param host location
   */
  // public void setWebSiteName(String host)
  // {
  //   webSiteName = host;
  // }   

  /** Set the location where the HTML files reside on the web site.
   * For example: 
   * <pre>
   * URLGenerator urlGen = new URLGenerator();
   * urlGen.setHTMLPath("/EJB_HTML");
   * </pre>
   *
   * @param p HTML files path
   */
  public void setHTMLPath(String p)
  {
    HTMLPath = p;
  }   


  /** Set the location where the script files reside on the web site.
   * For example: 
   * <pre>
   * URLGenerator urlGen = new URLGenerator();
   * urlGen.setScriptPath("/servlet");
   * </pre>
   *
   * @param p HTML files path
   */
  public void setScriptPath(String p)
  {
    scriptPath = p;
  }   


  /**
   * Set the value of http server port.
   *
   * @param p http server port
   */
  // public void setWebSitePort(int p) 
  // {
  //   webSitePort = p;
  // }


  /**
   * Return the string representing a float number in a format understandable by the database.
   * This function needs to be fixed since it still does not work with large number.
   *
   * @param f the float value to convert
   * @return a string representing the float conforming to database representation.
   */
  protected String convertFloatToStringDatabaseFormat(float f)
  {
    String result = Float.toString(f);
    int E = result.indexOf('E');
    if (E != -1)
    /* We have something like 1.2345E6 but the database needs 1.2345E+6
       So, we need to add the + */
      result = result.substring(0,E+1)+"+"+result.substring(E+1);
    return result;
  }

  private static Random r = new Random();
  protected URL getBaseURL()
  {
    synchronized (r) {
      return webSites.get(r.nextInt(webSites.size()));
    }
  }

  // ===========================================================
  // ==================== URL Generation =======================
  // ===========================================================

  private String aq(String filename)
  {
    if (extraQueryString.isEmpty())
      return filename;
    if (filename.indexOf('?') == -1)
      return filename + "?" + extraQueryString;
    else
      return filename + "&" + extraQueryString;
  }

  /** URL to the page corresponding to the file.
   *
   * @param filename file name
   * @return the URL corresponding to the file
   */
  public URL genericHTMLFile(String filename)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(filename));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating the URL corresponding to the file: "+e.getMessage());
      return null;
    }
  }

  // =====================================================
  // ==================== Home page ======================
  // =====================================================

  /** URL to the home page of the web site.
   *
   * @return home page URL
   */
  public URL homePage()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(HTMLPath+"/index.html"));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating home page URL: "+e.getMessage());
      return null;
    }
  }


  // =====================================================
  // ==================== Register =======================
  // =====================================================

  /** URL to the register user page of the web site.
   *
   * @return register user page URL
   */
  public URL register()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(HTMLPath+"/register.html"));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating register page URL: "+e.getMessage());
      return null;
    }
  }

  /** Register a new user in the database. You must provide all
   * information that are requested by the web page.
   *
   * @param firstname user's first name
   * @param lastname user's last name
   * @param nickname user's nick name (login name)
   * @param email user's email address
   * @param password user's password
   * @param regionName region name where the user live (warning it is case sensitive)
   *
   * @return URL to use to register a new user
   */
  public URL registerUser(String firstname, String lastname, String nickname, 
                         String email, String password,String regionName)
  {
    try
    {
      firstname  = URLEncoder.encode(firstname);
      lastname   = URLEncoder.encode(lastname);
      nickname   = URLEncoder.encode(nickname);
      email      = URLEncoder.encode(email);
      password   = URLEncoder.encode(password);
      regionName = URLEncoder.encode(regionName);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+RegisterUserScript()+"?firstname="+firstname+"&lastname="+
      lastname+"&nickname="+nickname+"&email="+email+"&password="+password+"&region="+regionName));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating register user page URL: "+e.getMessage());
      return null;
    }
  }


  // =====================================================
  // ==================== Browsing =======================
  // =====================================================

  /** URL to the browse page of the web site.
   *
   * @return browse page URL
   */
  public URL browse()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(HTMLPath+"/browse.html"));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating browse page URL: "+e.getMessage());
      return null;
    }
  }


  /** Access the Browse Categories page of RUBiS that lists all
   * available categories. The user can then select a category
   * to view all items in that category.
   *
   * @return Browse categories script URL
   */
  public URL browseCategories()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BrowseCategoriesScript()));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating Browse Categories script URL: "+e.getMessage());
      return null;
    }
  }

  /** Access the Browse Regions page of RUBiS that lists all
   * available region. The user can then select a region
   * to view all categories available in that region.
   *
   * @return Browse regions script URL
   */
  public URL browseRegions()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BrowseRegionsScript()));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating Browse Regions script URL: "+e.getMessage());
      return null;
    }
  }

  /**
   * URL to the 'browse all items in a category' script. You must
   * provide both the category id and category name.
   *
   * @param categoryId category id
   * @param categoryName category name
   * @param page page to view (0=first page)
   * @param nbOfItems number of items to display per page
   *
   * @return 'browse all items in a category' script URL
   */
  public URL browseItemsInCategory(int categoryId, String categoryName, int page, int nbOfItems)
  {
    try
    {
      categoryName = URLEncoder.encode(categoryName);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+SearchItemsByCategoryScript()+
                        "?category="+categoryId+"&categoryName="+categoryName+"&page="+page+"&nbOfItems="+nbOfItems));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'browse all items in a category' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'browse all categories in a region' script. You must
   * provide the region name.
   *
   * @param regionName region name
   *
   * @return 'browse all categories in a region' script URL
   */
  public URL browseCategoriesInRegion(String regionName)
  {
    regionName = URLEncoder.encode(regionName);
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BrowseCategoriesScript()+"?region="+regionName));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'browse all categories in a region' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'browse all items in a category in a region' script. You must
   * provide the category id, category name and region id.
   *
   * @param categoryId category id
   * @param categoryName category name
   * @param regionId region id
   * @param page page to view (0=first page)
   * @param nbOfItems number of items to display per page
   *
   * @return 'browse all items in a category in a region' script URL
   */
  public URL browseItemsInRegion(int categoryId, String categoryName, int regionId, int page, int nbOfItems)
  {
    try
    {
      categoryName = URLEncoder.encode(categoryName);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+SearchItemsByRegionScript()+
                        "?region="+regionId+"&category="+categoryId+"&categoryName="+categoryName+"&page="+page+"&nbOfItems="+nbOfItems));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'browse all items in a category in a region' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'View Item' script. You must provide the item id.
   *
   * @param itemId item identifier
   *
   * @return 'View Item' script URL
   */
  public URL viewItem(int itemId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+ViewItemScript()+"?itemId="+itemId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'View Item' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'View Bid History' script. You must provide the item id.
   *
   * @param itemId item identifier
   *
   * @return 'View Bid History' script URL
   */
  public URL viewBidHistory(int itemId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+ViewBidHistoryScript()+"?itemId="+itemId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'View Bid History' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'View User Information' script. You must provide the user id.
   *
   * @param userId user identifier
   *
   * @return 'View User Information' script URL
   */
  public URL viewUserInformation(int userId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+ViewUserInfoScript()+"?userId="+userId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'View User Information' script URL: "+e.getMessage());
      return null;
    }
  }


  // =====================================================
  // ==================== Comments =======================
  // =====================================================

  /** URL to the 'Comment on an user' script.
   * You must provide the item id and the user id of the receiver of this comment.
   * This will lead to a user authentification.
   *
   * @param itemId item identifier
   * @param toId receiver user identifier
   *
   * @return 'Comment on an user' script URL
   */
  public URL putCommentAuth(int itemId, int toId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+PutCommentAuthScript()+"?itemId="+itemId+"&to="+toId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Comment on an user' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Put Comment on another user' script.
   * This is the page the user can access when it has been successfully authenticated.
   *
   * @param itemId item identifier
   * @param toId receiver user identifier
   * @param name user's name
   * @param pwd user's password
   *
   * @return 'Put Comment on another user' script URL
   */
  public URL putComment(int itemId, int toId, String name, String pwd)
  {
    try
    {
      name = URLEncoder.encode(name);
      pwd  = URLEncoder.encode(pwd);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+PutCommentScript()+"?itemId="+itemId+"&to="+toId+"&nickname="+name+"&password="+pwd));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Put Comment on another user' script URL: "+e.getMessage());
      return null;
    }
  }   

  /** URL to the 'StoreComment' script. This action really stores a new comment in the database.
   *
   * @param itemId item identifier
   * @param toId receiver user identifier
   * @param fromId author user identifier
   * @param rating user's rating generally a value between -5 and +5
   * @param comment the comment text itself
   *
   * @return 'Store Comment' script URL
   */
  public URL storeComment(int itemId, int toId, int fromId, int rating, String comment)
  {
    try
    {
      comment = URLEncoder.encode(comment);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+StoreCommentScript()+"?itemId="+itemId+"&to="+toId+"&from="+fromId+"&rating="+rating+"&comment="+comment));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating StoreComment script URL: "+e.getMessage());
      return null;
    }
  }   


  // ====================================================
  // ==================== Bidding =======================
  // ====================================================

  /** URL to the 'Bid now on an Item' script. You must provide the item id.
   * This will lead to a user authentification.
   *
   * @param itemId item identifier
   *
   * @return 'Bid Now' script URL
   */
  public URL putBidAuth(int itemId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+PutBidAuthScript()+"?itemId="+itemId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Bid now on an Item' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Put bid on an Item' script.
   * This is the page the user can access when it has been successfully authenticated.
   * You must provide the item id, user name and password.
   *
   * @param itemId item identifier
   * @param name user's name
   * @param pwd user's password
   *
   * @return 'Put Bid' script URL
   */
  public URL putBid(int itemId, String name, String pwd)
  {
    try
    {
      name = URLEncoder.encode(name);
      pwd  = URLEncoder.encode(pwd);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+PutBidScript()+"?itemId="+itemId+"&nickname="+name+"&password="+pwd));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Put bid on an Item' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Store bid' script. This action really stores a new bid in the database.
   *
   * @param itemId item identifier
   * @param userId user identifier
   * @param minBid minimum authorized bid
   * @param bid user's bid
   * @param maxBid user's maximum bid
   * @param qty quantity user is asking for
   * @param maxQty total available quantity for selling
   *
   * @return 'Store Bid' script URL
   */
  public URL storeBid(int itemId, int userId, float minBid, float bid, float maxBid, int qty, int maxQty)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+StoreBidScript()+"?itemId="+itemId+"&userId="+userId+"&minBid="+convertFloatToStringDatabaseFormat(minBid)+"&maxQty="+maxQty+"&bid="+convertFloatToStringDatabaseFormat(bid)+"&maxBid="+convertFloatToStringDatabaseFormat(maxBid)+"&qty="+qty));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Store bid' script URL: "+e.getMessage());
      return null;
    }
  }   


  // ===================================================
  // ==================== Buying =======================
  // ===================================================

  /** URL to the 'Buy now an Item' script. You must provide the item id.
   * This will lead to a user authentification.
   *
   * @param itemId item identifier
   *
   * @return 'Buy Now' script URL
   */
  public URL buyNowAuth(int itemId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BuyNowAuthScript()+"?itemId="+itemId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Buy now auth' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'buy now' script. This action displays the item information and ask the user to confirm he want to buy it.
   *
   * @param itemId item identifier
   * @param name user's name
   * @param pwd user's password
   *
   * @return 'Buy Now' script URL
   */
  public URL buyNow(int itemId, String name, String pwd)
  {
    try
    {
      name = URLEncoder.encode(name);
      pwd  = URLEncoder.encode(pwd);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BuyNowScript()+"?itemId="+itemId+"&nickname="+name+"&password="+pwd));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Buy Now' script URL: "+e.getMessage());
      return null;
    }
  }   


  /** URL to the 'store buy now' script. This action really buys an item and update the database (if authentication is successfull).
   *
   * @param itemId item identifier
   * @param userId user's identifier
   * @return 'Store Buy Now' script URL
   */
  public URL storeBuyNow(int itemId, int userId, int qty, int maxQty)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+StoreBuyNowScript()+"?itemId="+itemId+"&userId="+userId+"&qty="+qty+"&maxQty="+maxQty));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Store Buy Now' script URL: "+e.getMessage());
      return null;
    }
  }   


  // ====================================================
  // ==================== Selling =======================
  // ====================================================

  /** URL to the sell page of the web site.
   *
   * @return sell page URL
   */
  public URL sell()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(HTMLPath+"/sell.html"));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating sell page URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Select category to sell item' script.
   * This is the page the user can access when it has been successfully authenticated.
   * You must provide the user's name and password.
   *
   * @param name user's name
   * @param pwd user's password
   *
   * @return 'Select category to sell item' script URL
   */
  public URL selectCategoryToSellItem(String name, String pwd)
  {
    try
    {
      name = URLEncoder.encode(name);
      pwd  = URLEncoder.encode(pwd);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+BrowseCategoriesScript()+"?nickname="+name+"&password="+pwd));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Select category to sell item' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Sell item form' script.
   * This is the page the user accesses ones it has choosen a category.
   * You must provide the user id and the category id.
   *
   * @param categoryId category identifier
   * @param userId user identifier
   *
   * @return 'Sell item form' script URL
   */
  public URL sellItemForm(int categoryId, int userId)
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+SellItemFormScript()+"?user="+userId+"&category="+categoryId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Sell item form' script URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'Register item' script. This action really stores a new item in the database.
   *
   * @param name item name
   * @param description item description
   * @param initialPrice item initial price
   * @param reservePrice item reserve price (0 if none)
   * @param buyNow item 'buy now' price (0 if none)
   * @param duration duration of the auction in days (usually no more than 7 days)
   * @param quantity quantity to sell of this item
   * @param userId user identifier
   * @param categoryId category identifier
   *
   * @return 'Register item' script URL
   */
  public URL registerItem(String name, String description, float initialPrice, 
                         float reservePrice, float buyNow, int duration,
                         int quantity, int userId, int categoryId)
  {
    try
    {
      name        = URLEncoder.encode(name);
      description = URLEncoder.encode(description);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+RegisterItemScript()+"?name="+name+"&description="+description+
                        "&initialPrice="+convertFloatToStringDatabaseFormat(initialPrice)+"&reservePrice="+convertFloatToStringDatabaseFormat(reservePrice)+
                        "&buyNow="+convertFloatToStringDatabaseFormat(buyNow)+"&duration="+duration+"&quantity="+quantity+"&userId="+userId+"&categoryId="+categoryId));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'Register item' script URL: "+e.getMessage());
      return null;
    }
  }

  // =====================================================
  // ==================== About Me =======================
  // =====================================================

  /** URL to the "About Me" page of the web site.
   * This leads to a user authentication.
   *
   * @return 'About Me' page URL
   */
  public URL aboutMe()
  {
    try
    {
      URL url = new URL(getBaseURL(), aq(HTMLPath+"/about_me.html"));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating about_me page URL: "+e.getMessage());
      return null;
    }
  }

  /** URL to the 'About Me' script.
   * This is the page the user can access when it has been successfully authenticated.
   * You must provide the user's name and password.
   *
   * @param name user's name
   * @param pwd user's password
   *
   * @return 'About Me' script URL
   */
  public URL aboutMe(String name, String pwd)
  {
    try
    {
      name = URLEncoder.encode(name);
      pwd  = URLEncoder.encode(pwd);
      URL url = new URL(getBaseURL(), aq(scriptPath+"/"+AboutMeScript()+"?nickname="+name+"&password="+pwd));
      return url;
    }
    catch (java.net.MalformedURLException e)
    {
      System.out.println("Error while generating 'About Me' script URL: "+e.getMessage());
      return null;
    }
  }

}