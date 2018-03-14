/**
 * 
 */
package tools.scrapper.web;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import tools.scrapper.logger.ConsoleWriter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Prajapati
 *
 */
public class WebScrapper {
	
	private static final ConsoleWriter log = new ConsoleWriter();
	
	@Parameter(names = { "--url", "-u" }, description = "URL to scrap for dynamic html content", required = true, help = true)
	private String url;
	
	public static void main(String[] args) {
		WebScrapper scrapper = new WebScrapper();
        JCommander.newBuilder()
            .addObject(scrapper)
            .build()
            .parse(args);
        scrapper.run();
	}

	private void run() {
		log.info("WebScrapper run() - START"); 
		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
			// Get the first page
			log.info("Get page from URL %s", url); 
	        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
	        webRequest.getAdditionalHeaders().put("Accept", "application/json, text/javascript, */*; q=0.01");
	        webRequest.getAdditionalHeaders().put("Accept-Encoding", "gzip, deflate");
//	        webRequest.getAdditionalHeaders().put("Accept-Encoding", "gzip, deflate");
			final HtmlPage page = webClient.getPage(webRequest);
			log.info("Saving page to 'scrapper.output.html'"); 
	        page.save(new File("scrapper.output.html"));
	        page.cleanUp();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			log.info("WebScrapper run() - END"); 
		}
	}
}
