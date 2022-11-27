package com.dou.home.report;

import com.dou.home.shared.CommonStrings;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.dou.home.shared.CommonStrings.REPORT_TEMPLATE_EXTENSION;
import static com.dou.home.shared.CommonStrings.REPORT_VIEW_EXTENSION;

@Component
public class FileSystemStorageService implements StorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemStorageService.class);

	@Override
	public void deleteAll() {
		//TODO: next time
	}

	@Override
	public JasperReport getJasperFile(String filename) {
		JasperReport jasperReport = null;
		try {
			if (this.jasperFileExists(filename)) {
				jasperReport = (JasperReport) JRLoader.loadObject(this.loadJasperFile(filename));
			} else {
				if (!this.jrxmlFileExists(filename))
					LOGGER.error("Not found jasper template with filename " + filename);

				String jrxml = this.loadJrxmlFile(filename);
				jasperReport = JasperCompileManager.compileReport(jrxml);
				JRSaver.saveObject(jasperReport, this.loadJasperFile(filename));
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while try to retrieve jasper file for " + filename, e);
		}
		return jasperReport;
	}

	@Override
	public boolean jrxmlFileExists(String file) {
		try {
			Path reportFile = Paths.get(file + REPORT_TEMPLATE_EXTENSION);
			if (Files.exists(reportFile))
				return true;
		} catch (Exception e) {
			LOGGER.error("Error while trying to get file URI", e);
		}
		return false;
	}

	@Override
	public boolean jasperFileExists(String file) {
		try {
		Path reportFile = Paths.get(file + REPORT_VIEW_EXTENSION);
		if (Files.exists(reportFile))
			return true;
		} catch (Exception e) {
			LOGGER.error("Error while trying to get file URI", e);
		}
		return false;
	}

	@Override
	public String loadJrxmlFile(String file) {
		try {
			Path reportFile = Paths.get(file + REPORT_TEMPLATE_EXTENSION);
			return reportFile.toString();
		} catch (Exception e) {
			LOGGER.error("Error while trying to get file prefix", e);
		}
		return CommonStrings.EMPTY_STRING;
	}

	@Override
	public File loadJasperFile(String file) {
		Path reportFile = Paths.get(file + REPORT_VIEW_EXTENSION);
		return reportFile.toFile();
	}

}
