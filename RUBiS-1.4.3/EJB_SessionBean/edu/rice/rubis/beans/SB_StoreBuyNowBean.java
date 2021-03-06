package edu.rice.rubis.beans;

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.Serializable;
import javax.transaction.UserTransaction;

/**
 * This is a stateless session bean used when a user buy an item.
 *  
 * @author <a href="mailto:cecchet@rice.edu">Emmanuel Cecchet</a> and <a href="mailto:julie.marguerite@inrialpes.fr">Julie Marguerite</a>
 * @version 1.1
 */

public class SB_StoreBuyNowBean implements SessionBean 
{
  protected SessionContext sessionContext;
  protected Context initialContext = null;
  protected DataSource dataSource = null;
  private UserTransaction utx = null;

  /**
   * Create a buyNow and update the item.
   *
   * @param itemId id of the item related to the comment
   * @param userId id of the buyer
   * @param qty quantity of items
   * @since 1.1
   */
  public void createBuyNow(Integer itemId, Integer userId, int qty) throws RemoteException
  {
    PreparedStatement stmt = null;
    ResultSet rs           = null;
    Connection conn        = null;

    utx = sessionContext.getUserTransaction();
    try
    {
      utx.begin();
      // Try to find the Item corresponding to the Item ID
      String now = TimeManagement.currentDateToString();
      int quantity;
      try 
      {
        conn = dataSource.getConnection();
        stmt = conn.prepareStatement("SELECT quantity, end_date FROM items WHERE id=?");
        stmt.setInt(1, itemId.intValue());
        rs = stmt.executeQuery();
        
      }
      catch (SQLException e)
      {
        try { stmt.close(); } catch (Exception ignore) {}
        try { conn.close(); } catch (Exception ignore) {}
        throw new RemoteException("Failed to execute Query for the item: " +e+"<br>");
      }
      PreparedStatement pstmt = null;
      try
      {
        if (rs.first())
        {
          quantity = rs.getInt("quantity");
          quantity = quantity -qty;
 
          if (quantity == 0)
          {
            pstmt = conn.prepareStatement("UPDATE items SET end_date=?,quantity=? WHERE id=?");
            pstmt.setString(1, now);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, itemId.intValue());
            pstmt.executeUpdate();
          }
          else
          {
            pstmt = conn.prepareStatement("UPDATE items SET quantity=? WHERE id=?");
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, itemId.intValue());
            pstmt.executeUpdate();
          }
          stmt.close();
          pstmt.close();
        }
      }
      catch (Exception e)
      {
        try { stmt.close(); } catch (Exception ignore) {}
        try { pstmt.close(); } catch (Exception ignore) {}
        try { conn.close(); } catch (Exception ignore) {}
        throw new RemoteException("Failed to update item's quantity: " +e+"<br>");
      }
      try 
      {
        stmt = conn.prepareStatement("INSERT INTO buy_now VALUES (NULL, \""+userId.intValue()+
                                     "\", \""+itemId.intValue()+"\", \""+qty+"\", \""+now+"\")");
        stmt.executeUpdate();
      }
      catch (Exception e)
      {
        try { stmt.close(); } catch (Exception ignore) {}
        try { conn.close(); } catch (Exception ignore) {}
        throw new RemoteException("Failed to create buy_now item: " +e+"<br>");
      }
      if (stmt != null) stmt.close();
      if (conn != null) conn.close();
      utx.commit();

    } 
    catch (Exception e)
    {
      try { stmt.close(); } catch (Exception ignore) {}
      try { conn.close(); } catch (Exception ignore) {}
      try
      {
        utx.rollback();
        throw new RemoteException("Cannot insert the item into buy_now items table: " +e+"<br>");
      }
      catch (Exception se) 
      {
        throw new RemoteException("Transaction rollback failed: " + e +"<br>");
      }
    }
  }



  // ======================== EJB related methods ============================

  /**
   * This method is empty for a stateless session bean
   */
  public void ejbCreate() throws CreateException, RemoteException
  {
  }

  /** This method is empty for a stateless session bean */
  public void ejbActivate() throws RemoteException {}
  /** This method is empty for a stateless session bean */
  public void ejbPassivate() throws RemoteException {}
  /** This method is empty for a stateless session bean */
  public void ejbRemove() throws RemoteException {}


  /** 
   * Sets the associated session context. The container calls this method 
   * after the instance creation. This method is called with no transaction context. 
   * We also retrieve the Home interfaces of all RUBiS's beans.
   *
   * @param sessionContext - A SessionContext interface for the instance. 
   * @exception RemoteException - Thrown if the instance could not perform the function 
   *            requested by the container because of a system-level error. 
   */
  public void setSessionContext(SessionContext sessionContext) throws RemoteException
  {
    this.sessionContext = sessionContext;
    if (dataSource == null)
    {
      // Finds DataSource from JNDI
 
      try
      {
        initialContext = new InitialContext(); 
        dataSource = (DataSource)initialContext.lookup("java:comp/env/jdbc/rubis");
      }
      catch (Exception e) 
      {
        throw new RemoteException("Cannot get JNDI InitialContext");
      }
    }
  }

}
