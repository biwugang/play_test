package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import play.libs.Json;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	/**
	 * An action that renders an HTML page with a welcome message. The
	 * configuration in the <code>routes</code> file means that this method will
	 * be called when the application receives a <code>GET</code> request with a
	 * path of <code>/</code>.
	 */
	public Result index() {

		try {
			Config config = ConfigFactory.load("test.properties");

			String restBaseUrl = "rest.baseUrl";
			String webBaseUrl = "web.baseUrl";

			ObjectNode result = Json.newObject();
			result.put(restBaseUrl, config.getString(restBaseUrl));
			result.put(webBaseUrl, config.getString(webBaseUrl));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(index.render());
	}

	public Result getConfig() {
		ObjectNode result = Json.newObject();
		try {
			Config config = ConfigFactory.load("test.properties");

			String restBaseUrl = "rest.baseUrl";
			String webBaseUrl = "web.baseUrl";

			result.put(restBaseUrl, config.getString(restBaseUrl));
			result.put(webBaseUrl, config.getString(webBaseUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(result);
	}

	public Result test() {
		System.out.println("test() start ");
		String interfaceName = "eth0";
		NetworkInterface networkInterface;
		String ip = "127";
		try {
			networkInterface = NetworkInterface.getByName(interfaceName);

			Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
			InetAddress currentAddress;
			currentAddress = inetAddress.nextElement();
			while (inetAddress.hasMoreElements()) {
				currentAddress = inetAddress.nextElement();
				if (currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
					ip = currentAddress.toString();
					System.out.println("test() ip is: "+ip);
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok("hello, ip is:"+ip);
	}

	public Result testwithName(String name) {

		return ok("hello");
	}

	public Result login() {

		return ok(login.render());
	}

}
