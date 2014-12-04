package com.king.gis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;

public class Write2File {
	private static transient final Logger LOGGER = BeanUtil
			.getLogger(Write2File.class);

	public static void write2File(String filePath, String content) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(content.getBytes());
			out.close();
			LOGGER.info(String.format("%s OK", filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
