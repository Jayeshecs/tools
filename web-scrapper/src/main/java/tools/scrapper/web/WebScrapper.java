/**
 * 
 */
package tools.scrapper.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tools.scrapper.logger.CompositeLogWriter;
import tools.scrapper.logger.ConsoleWriter;
import tools.scrapper.logger.FileLogWriter;
import tools.scrapper.logger.ILogWriter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

/**
 * @author Prajapati
 *
 */
public class WebScrapper {
	
	private static final ILogWriter log = new CompositeLogWriter(new ConsoleWriter(), new FileLogWriter(new File("web-scrapper.log")));
	
	@Parameter(names = { "--url", "-u" }, description = "URL to scrap for dynamic html content", required = true, help = true)
	private String url;
	
	@Parameter(names = { "--debug", "-d" }, description = "Debug flag to log request & response of every page getting loaded", required = false, help = false)
	private Boolean debug = Boolean.FALSE;
	
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
		try (final WebClient webClient = new WebClient()) {
			
			registerRequestResponseLogger(webClient);
			// Get the first page
			log.info("Get page from URL %s", url); 
	        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			log.info("Saving page to 'scrapper.output.html'"); 
	        page.save(new File("scrapper.output.html"));
			List<HtmlForm> forms = page.getForms(); //ByXPath("//form[@method='post']");
			for (HtmlForm form : forms) {
				if (form.getAttributeNode("data-bld") != null) {
					ScriptResult result = page.executeJavaScript("$.otpCode('form', {'bld' : '" + form.getAttributeNode("data-bld") + "'})");
					String code = String.valueOf(result.getJavaScriptResult());
					String body = "market_gubun=ALL&isu_cdnm=&isu_cd=&isu_nm=&isu_srt_cd=&sort_type=&std_ind_cd=&par_pr&cpta_scl=&sttl_trm=&lst_stk_vl=1&in_lst_stk_vl=&in_lst_stk_vl2=&cpt=1&in_cpt=&in_cpt2=&pagePath=%2Fcontents%2FGLB%2F05%2F0503%2F0503010100%2FGLB0503010100.jsp&code=" + code;
			        WebRequest webRequest2 = new WebRequest(new URL(url.substring(0, url.indexOf('/', 10)) + form.getAttribute("action")), HttpMethod.POST);
			        webRequest2.setRequestBody(body);
					WebResponse response = webClient.getWebConnection().getResponse(webRequest2);
					String responseStr = response.getContentAsString();
					log.info("Response: %s", responseStr);
					break;
				}
			}
//			if (elements != null && !elements.isEmpty()) {
//				log.info("elements with class 'btn-board btn-board-search': %d", elements.size());
//				DomElement element = elements.get(0);
//				log.info("Search button: %s", element.asText());
//				log.info("Performing search by clicking on Search button");
//				page = element.click();
//				log.info("Saving page after click to 'scrapper.output.afterClick.html'"); 
//				page.save(new File("scrapper.output.afterClick.html"));
//			}
	        page.cleanUp();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.info("WebScrapper run() - END"); 
			try {
				log.getWriter().close();
			} catch (IOException e) {
				// DO NOTHING
				e.printStackTrace(new PrintWriter(log.getWriter()));
			}
		}
	}

	/**
	 * if you want to log every request and response detail during page load
	 * 
	 * @param webClient
	 */
	private void registerRequestResponseLogger(WebClient webClient) {
		if (!debug) {
			return ;
		}
		new WebConnectionWrapper(webClient) {
			@Override
			public WebResponse getResponse(WebRequest request) throws IOException {
				log.info("Request: %s", request.getUrl());
				WebResponse response = super.getResponse(request);
				log.info("Response: %s [Status: %s, Content: %s (%s)]", request.getUrl(), response.getStatusMessage(), response.getContentType(), response.getContentAsString());
				return response;
			}
		};
	}
}
