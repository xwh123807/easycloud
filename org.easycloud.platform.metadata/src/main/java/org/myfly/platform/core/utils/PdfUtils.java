package org.myfly.platform.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class PdfUtils {
	private static Log log = LogFactory.getLog(PdfUtils.class);

	public static void exportToPdf(String baseUrl, String contentUrl, String pdfFile) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
			InputStream contentInputStream = new URL(baseUrl + contentUrl).openConnection().getInputStream();
			String cssUrl = baseUrl + "/css/print-pdf.css";
			InputStream cssInputStream = new URL(cssUrl).openConnection().getInputStream();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, contentInputStream, cssInputStream,
					Charset.forName("UTF-8"));
			document.close();
			if (log.isInfoEnabled()) {
				log.info("将页面[" + contentUrl + "]生成pdf文件[" + pdfFile + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[" + contentUrl + "]转换为pdf文件[" + pdfFile + "]失败. " + e.getMessage());
		}
	}

	public static void exportToPdf(InputStream contentStream, InputStream cssStream, String pdfFile) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, contentStream, cssStream,
					Charset.forName("UTF-8"));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportToPdf(File contentFile, File cssFile, String pdfFile) throws Exception{
		InputStream contentStream = new FileInputStream(contentFile);
		InputStream cssStream = null;
		if (cssFile != null){
			cssStream = new FileInputStream(cssFile);
		}
		exportToPdf(contentStream, cssStream, pdfFile);
	}

	public static void exportToPdf2(String baseUrl, String contentUrl, String pdfFile) {
		try {
			InputStream contentInputStream = new URL(baseUrl + contentUrl).openConnection().getInputStream();
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
			// 使用我们的字体提供器，并将其设置为unicode字体样式
			CssAppliers cssAppliers = new CssAppliersImpl();
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
					new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));
			XMLWorker worker = new XMLWorker(pipeline, true);
			XMLParser p = new XMLParser(worker);
			p.parse(new InputStreamReader(contentInputStream, "UTF-8"));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("[" + contentUrl + "]转换为pdf文件[" + pdfFile + "]失败. " + e.getMessage());
		}
	}

}
