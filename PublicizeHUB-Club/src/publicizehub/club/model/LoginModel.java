package publicizehub.club.model;

import static java.lang.Long.parseLong;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 *
 * @author budsagorn_ss
 */
public class LoginModel {
    private static final Logger LOGGER = Logger.getLogger(LoginModel.class.getName());

    ConnectionBuilder cb = new ConnectionBuilder();
    
    private long stdId;
    private String name;
    private String department;
    private int status;

    public long getStdId() {
        return stdId;
    }

    public void setStdId(long stdId) {
        this.stdId = stdId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LoginModel() {
    }

    public LoginModel(long stdId, String name, String department, int status) {
        this.stdId = stdId;
        this.name = name;
        this.department = department;
        this.status = status;
    }
    
    private final static String ldapURI = "ldap://10.1.130.12:389/ou=people,ou=st,dc=kmutt,dc=ac,dc=th";
    private final static String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    
    public ResultSet selectLogin(long stdId) {
        PreparedStatement ps;
        ResultSet result = null;
        cb.connecting();
        try {
            ps = cb.getConnect().prepareStatement("SELECT * FROM students where std_id=? ");
            ps.setLong(1, stdId );
            result = ps.executeQuery();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "selectLogin : selectLogin Failed");
        }
        return result;
    }
    
    private static DirContext ldapContext () throws Exception {
		Hashtable<String,String> env = new Hashtable <String,String>();
		return ldapContext(env);
	}

    private static DirContext ldapContext (Hashtable <String,String>env) throws Exception {
            env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
            env.put(Context.PROVIDER_URL, ldapURI);
            DirContext ctx = new InitialDirContext(env);
            return ctx;
    }

    private static String getUid (String user , Label welcome) throws Exception {
            DirContext ctx = ldapContext();

            String filter = "(uid=" + user + ")";
            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration answer = ctx.search("", filter, ctrl);

            String dn;
            if (answer.hasMore()) {
                    SearchResult result = (SearchResult) answer.next();

                    Attributes attrs = result.getAttributes();
//                        System.out.println(attrs);
                    String tempName = attrs.get("cn")+"";
                    welcome.setText("Hi , " + tempName.substring(4));

                    dn = result.getNameInNamespace();
            }
            else {
                    dn = null;
                    welcome.setText("Error Valid username , password");
            }
            answer.close();
            return dn;
    }

    private static boolean checkId (String dn, String password) throws Exception {
            Hashtable<String,String> env = new Hashtable <String,String>();
            env.put(Context.SECURITY_AUTHENTICATION, "none");
            env.put(Context.SECURITY_PRINCIPAL, dn);
            env.put(Context.SECURITY_CREDENTIALS, password);

            try {
                    ldapContext(env);
            }
            catch (javax.naming.AuthenticationException e) {
                    return false;
            }
            return true;
    }
        
    public LoginModel login (String name , String password,Label welcome) throws Exception{
        LoginModel profile = null;
        String username = "";
        if(name!=null){
            username = name;
        }
        String dn = getUid( username , welcome );

        if (dn != null) {
            if ( checkId( dn, password ) ) {
                long stdId = parseLong(username);
                ResultSet prof = selectLogin(stdId);
                if(prof.next()){
                    profile = new LoginModel(stdId,prof.getString("std_name"),
                            prof.getString("std_faculty"),prof.getInt("std_status"));
                }
            } else {
                    welcome.setText( "invalid username or password" );
            }
        } else {
                welcome.setText( "invalid username or password" );
        }
        return profile;
    }
    
}
