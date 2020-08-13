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
import java.util.Properties;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author linux
 */
@WebServlet(name = "query", urlPatterns =
{
	"/query"
})
public class query extends HttpServlet
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
		long start = System.currentTimeMillis();
		response.setContentType("application/json;charset=UTF-8");
		String json = request.getParameter("search");
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		this.log("query=" + json);
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
				out.println("{");
				out.print("\"headers\": ");
				out.print(resp.first().getJsonHeaders((null!=bevUrl)));
				out.println(",");
				out.print("\"data\": ");
				out.println("[");
				boolean first = true;
				for (Dataset ds : resp)
				{
					if (true==first)
					{
						first = false;
					}
					else
					{
						out.println(",");
					}
					out.print(ds.getJson(gson, stdUrl, bevUrl));
				}
				out.println("");
				out.println("]");
				out.println("}");
			}
			else
			{
				this.log("query - no indexes yet");
			}
		}
		long finish = System.currentTimeMillis();
		this.log("query time = " + ((finish - start)/1000.00) + " seconds" );
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
