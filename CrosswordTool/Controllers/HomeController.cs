using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrosswordTool.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index(int? rows,int? cols)
        {
            ViewBag.Message = "Tạo ô chữ";
            //for (int i = 0; i < rows; i++) {
            //    for (int j = 0; j < cols; j++) { 

            //    }
            //}
            ViewBag.rows = 0;
            ViewBag.cols = 0;
            if (rows != null && cols != null)
            {
                ViewBag.rows = rows;
                ViewBag.cols = cols;
            }
            ViewBag.date = DateTime.Now.ToString();
            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your app description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}
