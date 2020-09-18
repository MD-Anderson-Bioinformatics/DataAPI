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

package edu.mda.bcb.dapi.servlets;

import edu.mda.bcb.dapi.indexes.Indexes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.util.Streams;

/**
 *
 * @author Tod-Casasent
 */
@WebServlet(name = "download", urlPatterns =
{
	"/download"
})
public class download extends HttpServlet
{

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("application/zip;charset=UTF-8");
		try (ServletOutputStream out = response.getOutputStream())
		{
			Indexes myIndexes = null;
			String show = request.getParameter("show");
			if ("dsc".equals(show))
			{
				myIndexes = (Indexes)(this.getServletContext().getAttribute("DSC-INDEXES"));
			}
			else
			{
				myIndexes = (Indexes)(this.getServletContext().getAttribute("INDEXES"));
			}
			String id = request.getParameter("id");
			this.log("download::processRequest id=" + id);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".zip\"");
			File zip = myIndexes.getPath(id);
			// single simultaneous downloads
			stream(zip, out, response);
		}
		catch (Exception exp)
		{
			log("download::processRequest failed", exp);
			response.setStatus(500);
			response.sendError(500, exp.getMessage());
		}
	}
	
	synchronized static public void stream(File theZip, ServletOutputStream theOut, HttpServletResponse theResponse) throws FileNotFoundException, IOException
	{
		// single simultaneous downloads
		try(FileInputStream input = new FileInputStream(theZip))
		{
			Streams.copy(input, theOut, true);
			theResponse.setStatus(200);
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo()
	{
		return "Short description";
	}// </editor-fold>

}
