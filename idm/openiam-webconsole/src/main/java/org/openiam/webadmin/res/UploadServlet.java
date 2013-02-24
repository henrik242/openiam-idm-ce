package org.openiam.webadmin.res;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.util.TempFile;
import org.apache.xmlbeans.impl.common.ReaderInputStream;
import org.springframework.util.StringUtils;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

public class UploadServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TMP_DIR_PATH = "/tmp";
	private File tmpDir;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if (!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		/*
		 * Set the size threshold, above which content will be stored on disk.
		 */
		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
		/*
		 * Set the temporary directory to store the uploaded files of size above
		 * threshold.
		 */
		fileItemFactory.setRepository(tmpDir);

		String DESTINATION_DIR_PATH = "";
		String FILE_NAME = "";
		String resId = "";
		File destinationDir;

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			FileItem resultItem = null;
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if (!item.isFormField()) {
					resultItem = item;
				} else {
					if ("recName".equals(item.getFieldName())) {
						FILE_NAME = item.getString();
					}
					if ("csvDir".equals(item.getFieldName())) {
						DESTINATION_DIR_PATH = item.getString();
					}if ("rId".equals(item.getFieldName())) {
						resId = item.getString();
					}
				}
			}
			if (!StringUtils.hasText(DESTINATION_DIR_PATH)) {
				throw new ServletException("DESTINATION_DIR_PATH is empty");
			}

			if (!StringUtils.hasText(FILE_NAME)) {
				throw new ServletException("FILE_NAME is empty");
			}

			destinationDir = new File(DESTINATION_DIR_PATH);
			if (!destinationDir.isDirectory()) {
				throw new ServletException(DESTINATION_DIR_PATH
						+ " is not a directory");
			}

			File file = new File(destinationDir, FILE_NAME);
			resultItem.write(file);
			log("File successfuly uploaded to: " + DESTINATION_DIR_PATH
					+ FILE_NAME);
			// TODODevelopment
			response.sendRedirect(request.getContextPath()
					+ "/reconcilConfig.cnt?menuid=RESRECONCILE&menugrp=SECURITY_RES&objId="+resId);
		} catch (FileUploadException ex) {
			log("Error encountered while parsing the request", ex);
		} catch (Exception ex) {
			log("Error encountered while uploading file", ex);
		}

	}
}
