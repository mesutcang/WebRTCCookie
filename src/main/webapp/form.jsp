<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WebRTC Cookie</title>
</head>
<body>
<form action="/WebRTCCookie/webrtccookie" method="POST">
<table>
    <tr>
        <td>Softphone (used in HTTP redirect):</td>
        <td><input type="text" name="softphone_link" value="http://tryit.jssip.net"/></td>
    </tr>
    <tr>
        <td>Domain part of "ws:" URI, used in setting cookie domain:</td>
        <td><input type="text" name="proxy_domain" value=""/></td>
    </tr>
    <tr>
        <td>From:</td>
        <td><input type="text" name="sip_from" value=""/></td>
    </tr>
    <tr>
        <td>To:</td>
        <td><input type="text" name="sip_to" value=""/></td>
    </tr>
    <tr>
        <td>HMAC key (must match repro.config WSCookieAuthSharedSecret):</td>
        <td><input type="text" name="hmac_key" value=""/></td>
    </tr>
    <tr>
        <td>Timeout (default now + 1000s): </td>
        <td><input type="text" name="time_limit" value="<%= (System.currentTimeMillis() / 1000L) +1000 %>"/></td>
    </tr>
    <tr>
        <td>Extra header value:</td>
        <td><input type="text" name="extra_value" value=""/></td>
    </tr>
    <tr>
        <td></td>
        <td><input type="submit" /></td>
    </tr>
</table>
     <br/>
    <p>Note: for from and to URIs, you may use "*" to replace the user, host or both, e.g. *@example.org would allow calls to anybody in example.org</p>
</form>
<h3>Usage notes</h3>
<ul>
    <li>enable a WS transport and WSCookieAuthSharedSecret in repro.config</li>
    <li>browse to this page</li>
    <li>set proxy_domain to exactly match the domain used by the WS transport</li>
    <li>NOTE: proxy_domain must also match the domain where this page is hosted otherwise the browser doesn't seem to accept the cookie.
        <ul>
            <li>You can strip the host portion of the domain, </li>
            <li>host the page on www.example.org,</li>
            <li>proxy ws://sip.example.org</li>
            <li>use proxy_domain = example.org to serve both</li>
        </ul>
    </li>
    <li>set sip_from and sip_to to restrict the callee and caller URIs, or just use wildcards like *@*</li>
    <li>set the HMAC key to match the key in repro.config</li>
    <li>make sure you connect within the specified time_limit</li>
</ul>
<p>For a full overview, <a href="http://www.resiprocate.org/SIP_Over_WebSocket_Cookies">see the cookie page on the reSIProcate wiki</a></p>

</body>
</html>