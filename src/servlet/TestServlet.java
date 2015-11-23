package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tdt.DataPreprocessor;
import tdt.Glossary;
import tdt.Story;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("doget");
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());

		//System.out.println("doget" + " " + request.getSession().getServletContext().getRealPath(""));
//		String[] datasetDir = new String[] { "D:/Jee_workspace/TopicDetectionAndTracking/Dataset/" };
//		tdt.Main.main(datasetDir);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		try {
			out.println("test");
		} finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("dopost");

		// doGet(request, response);

		// response.setContentType("text/html; charset=UTF-8");
		// response.setHeader("Cache-Control", "no-cache");

		// StringBuffer xml = new StringBuffer("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?><items>");
		//
		// xml.append("<code>0</code>");
		//
		// xml.append("</items>");
		//
		// PrintWriter out = response.getWriter();
		// out.println(xml.toString());
		// out.flush();
		// out.close();

		Vector<Story> corpus = new Vector<Story>();
		Glossary glossary = new Glossary();
		Vector<Story> actualFirstStories = new Vector<Story>();

		// Used to record the files list containing a certain word.
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices = new HashMap<Integer, HashSet<Integer>>();

		String sgmDir = "Dataset/sgm/";
		String ansFile = "Dataset/answer.txt";

		DataPreprocessor dataPreprocessor = new DataPreprocessor();
		// dataPreprocessor.doDataPreprocessing(corpus, glossary,
		// wordIDToStoryIndices, actualFirstStories, tknDir, bndDir, ansFile);
		dataPreprocessor.doDataPreprocessing(corpus, glossary, wordIDToStoryIndices, actualFirstStories, sgmDir,
				ansFile);

		System.out.println("corpus: " + corpus.get(0).getTimeStamp());

		request.setAttribute("corpus", corpus);
		request.getRequestDispatcher("main.jsp").forward(request, response);

	}

}
