package org.webrtc.cookie;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

@WebServlet("/webrtccookie")
public class WebRTCCookie extends HttpServlet{

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().log("Page is loading.");
        RequestDispatcher view = req.getRequestDispatcher("form.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("softphone_link") != null && req.getParameter("softphone_link").length() != 0) {
            String proxy_domain = req.getParameter("proxy_domain");
            String time_limit = req.getParameter("time_limit");
            String cookie_value = new StringBuilder().append("1:")
                                                     .append(System.currentTimeMillis() / 1000L)
                                                     .append(":")
                                                     .append(time_limit)
                                                     .append(":")
                                                     .append(req.getParameter("sip_from"))
                                                     .append(":")
                                                     .append(req.getParameter("sip_to")).append("+").toString();
            String extra_value = req.getParameter("extra_value");
            String digest_input = cookie_value + ":" + extra_value;
            String cookie_mac = null;
            try {
                cookie_mac = calculateRFC2104HMAC(digest_input, req.getParameter("hmac_key"));
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            Cookie wsSessionInfo = new Cookie("WSSessionInfo", URLEncoder.encode(cookie_value,"utf-8"));
            wsSessionInfo.setPath("/");
            wsSessionInfo.setDomain(proxy_domain);
            wsSessionInfo.setMaxAge(Integer.parseInt(time_limit));
            resp.addCookie(wsSessionInfo);

            Cookie wsSessionExtra = new Cookie("WSSessionExtra", URLEncoder.encode(extra_value,"utf-8"));
            wsSessionExtra.setPath("/");
            wsSessionExtra.setDomain(proxy_domain);
            wsSessionExtra.setMaxAge(Integer.parseInt(time_limit));
            resp.addCookie(wsSessionExtra);

            Cookie wsSessionMAC = new Cookie("WSSessionMAC", URLEncoder.encode(cookie_mac,"utf-8"));
            wsSessionMAC.setPath("/");
            wsSessionMAC.setDomain(proxy_domain);
            wsSessionMAC.setMaxAge(Integer.parseInt(time_limit));
            resp.addCookie(wsSessionMAC);

        }else{
            resp.getWriter().write("You need to provide softphone_link!");
        }
    }
    private final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public String calculateRFC2104HMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }
}