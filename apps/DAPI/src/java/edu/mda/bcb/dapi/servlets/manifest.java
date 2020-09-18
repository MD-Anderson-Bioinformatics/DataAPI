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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.mda.bcb.dapi.indexes.Indexes;
import edu.mda.bcb.dapi.query.Dataset;
import edu.mda.bcb.dapi.query.Query;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tod-Casasent
 */
@WebServlet(name = "manifest", urlPatterns =
{
	"/manifest"
})
public class manifest extends HttpServlet
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
		response.setContentType("application/text;charset=UTF-8");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmm");
		response.setHeader("Content-Disposition", "attachment; filename=\"manifest-" + dateFormat.format(calendar.getTime()) + ".tsv\"");
		String json = request.getParameter("search");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		this.log("manifest=" + json);
		Query queryInst = gson.fromJson(json, Query.class);
		try (PrintWriter out = response.getWriter())
		{
			Indexes myIndexes = null;
			String show = request.getParameter("show");
			String stdUrl = null;
			String bevUrl = null;
			if ("dsc".equals(show))
			{
				myIndexes = (Indexes)(this.getServletContext().getAttribute("DSC-INDEXES"));
				stdUrl = ((Properties)(this.getServletContext().getAttribute("PROPS"))).getProperty("STD_URL");
				bevUrl = ((Properties)(this.getServletContext().getAttribute("PROPS"))).getProperty("BEV_URL");
			}
			else
			{
				myIndexes = (Indexes)(this.getServletContext().getAttribute("INDEXES"));
				stdUrl = ((Properties)(this.getServletContext().getAttribute("PROPS"))).getProperty("STD_URL");
				if (myIndexes.mIsBEV)
				{
					bevUrl = ((Properties)(this.getServletContext().getAttribute("PROPS"))).getProperty("BEV_URL");
				}
			}
			if (null!=myIndexes)
			{
				TreeSet<Dataset> resp = queryInst.process(myIndexes);
				if (resp.size()>0)
				{
					out.println(resp.first().getHeaders((null!=bevUrl)));
					for (Dataset ds : resp)
					{
						out.println(ds.toString(stdUrl, bevUrl));
					}
				}
				else
				{
					out.println("");
				}
			}
			else
			{
				this.log("query - no indexes yet");
			}
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
