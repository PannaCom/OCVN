using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using CrosswordTool.Models;
using Newtonsoft.Json;
namespace CrosswordTool.Controllers
{
    public class HomeController : Controller
    {
        private binhyenEntities db = new binhyenEntities();
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
        public string updateRanking(string iddevice, string namedevice, string username, int point, int levels)
        {
            try
            {
                var id = db.crosswords.Where(o => o.iddevice.Contains(iddevice)).FirstOrDefault();
                if (id == null)
                {
                    crossword cr = new crossword();
                    cr.iddevice = iddevice;
                    cr.namedevice = namedevice;
                    cr.username = username;
                    cr.point = point;
                    cr.levels = levels;
                    cr.loggedate = DateTime.Now;
                    db.crosswords.Add(cr);
                    db.SaveChanges();
                    return "Cập nhật thành công!";
                }
                else
                {
                    string query = "update crossword set point=" + point + ",levels=" + levels + ",loggedate='" + DateTime.Now.ToString() + "',username=N'" + username + "' where iddevice=N'" + iddevice + "'";
                    db.Database.ExecuteSqlCommand(query);
                    return "Cập nhật thành công!";
                }
            }
            catch (Exception ex)
            {
                return "Cập nhật không thành công! Kiểm tra lại đường truyền!";
            }
        }
        public string addFeedBack(string content) {
            try
            {
                feedback fb = new feedback();
                fb.contents = content;
                db.feedbacks.Add(fb);
                db.SaveChanges();
                return "Gửi phản hồi thành công, cảm ơn sự góp ý của bạn";
            }
            catch (Exception ex) {
                return "Cập nhật không thành công! Kiểm tra lại đường truyền!";
            }
            
        }
        public string getRankingList()
        {
            try
            {
                var p = (from q in db.crosswords select q).OrderByDescending(o => o.point).ThenBy(o => o.levels).Take(100);
                return JsonConvert.SerializeObject(p.ToList());
            }
            catch (Exception ex)
            {
                return "";
            }
        }
    }
}
