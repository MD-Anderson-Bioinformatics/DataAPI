// Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020 University of Texas MD Anderson Cancer Center
//
// This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// MD Anderson Cancer Center Bioinformatics on GitHub <https://github.com/MD-Anderson-Bioinformatics>
// MD Anderson Cancer Center Bioinformatics at MDA <https://www.mdanderson.org/research/departments-labs-institutes/departments-divisions/bioinformatics-and-computational-biology.html>

package edu.mda.bcb.bev.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author Tod-Casasent
 */
public class ZipUtil
{
	static public void streamFile(String theZip, String theFile, ServletOutputStream theOut, HttpServlet theServlet) throws IOException
	{
		theServlet.log("streamFile theZip=" + theZip);
		theServlet.log("streamFile theFile=" + theFile);
		try(ZipFile zf = new ZipFile(new File(theZip)))
		{
			Enumeration<? extends ZipEntry> entries = zf.entries();
			while(entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals(theFile))
				{
					try(InputStream is = zf.getInputStream(entry))
					{
						IOUtils.copy(is, theOut);
					}
					return;
				}
			}
		}
	}
	
	static public void streamImageB64(String theZip, String theFile, ServletOutputStream theOut, HttpServlet theServlet) throws IOException
	{
		theServlet.log("streamImageB64 theZip=" + theZip);
		theServlet.log("streamImageB64 theFile=" + theFile);
		try(ZipFile zf = new ZipFile(new File(theZip)))
		{
			Enumeration<? extends ZipEntry> entries = zf.entries();
			while(entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (entry.getName().equals(theFile))
				{
                                    theServlet.log("matched");
                                    try(InputStream is = zf.getInputStream(entry))
                                    {
                                            try (Base64OutputStream newOut = new Base64OutputStream(theOut))
                                            {
                                                    IOUtils.copy(is, newOut);
                                            }
                                    }
                                    return;
				}
			}
		}
	}
}
