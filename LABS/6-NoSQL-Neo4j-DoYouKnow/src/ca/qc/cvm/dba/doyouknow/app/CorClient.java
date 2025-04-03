package ca.qc.cvm.dba.doyouknow.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.objectif8.libraries.common.network.ClientAdapter;
import com.objectif8.libraries.common.network.NetworkAdapter;
import com.objectif8.libraries.common.network.NetworkEvent;
import com.objectif8.libraries.common.network.StringListMessage;
import com.objectif8.libraries.common.util.FileUtil;

import ca.qc.cvm.dba.correctionserver.lib.BaseCorClient;
import ca.qc.cvm.dba.doyouknow.dao.UserDAO;

public class CorClient extends BaseCorClient {
	private UserDAO userDAO;
	
	public CorClient(UserDAO graphDAO) {
		this.userDAO = graphDAO;
	}
	
	@Override
	protected void executeTests(List<String> res, List<String> info) {
		for (String line : info) {
			if (line.startsWith("RUN;")) {
				line = line.replace("RUN;", "");
				
				try {
					if (line.startsWith("findAllUsers")) {
						res.add(this.userDAO.findAllUsers().toString());
					}
					else if (line.startsWith("getDirectConnectionsOf")) {
						line = line.replace("getDirectConnectionsOf/", "");
						res.add(this.userDAO.getDirectConnectionsOf(line).toString());
					}
					else if (line.startsWith("proposeConnection")) {
						line = line.replace("proposeConnection/", "");
						res.add(this.userDAO.proposeConnection(line).toString());
					}
					else if (line.startsWith("getPopularUsers")) {
						res.add(this.userDAO.getPopularUsers().toString());
					}
					else if (line.startsWith("checkUnconnected")) {
						res.add(this.userDAO.checkUnconnected().toString());
					}
					else if (line.startsWith("getOldest")) {
						res.add(this.userDAO.getOldest().toString());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					res.add("err");
				}
			}
		}
	}
}
