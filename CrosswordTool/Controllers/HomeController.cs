using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using CrosswordTool.Models;
using Newtonsoft.Json;
using iTextSharp.text.pdf;
using iTextSharp.text.pdf.parser;
using System.Text;
using System.IO;
namespace CrosswordTool.Controllers
{
    public class HomeController : Controller
    {
        private binhyenEntities db = new binhyenEntities();
        public class Items
        {
            public string view;
        }
        public ActionResult Index(int? rows,int? cols,string file)
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
            if (rows == null || cols == null) return View();
            string path = HttpContext.Server.MapPath("") + "/OchuRaw\\"+file + ".txt";
            StreamReader SR = new StreamReader(path);
            rows=int.Parse(SR.ReadLine());
            cols = int.Parse(SR.ReadLine());
            ViewBag.Items=new Items[(int)rows];
            for (int i = 0; i < rows; i++)
            {
                ViewBag.Items[i] = new Items();
                ViewBag.Items[i].view = "";
                ViewBag.Items[i].view = SR.ReadLine();
            }
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
                var p = (from q in db.crosswords select q).OrderByDescending(o => o.point).ThenByDescending(o => o.levels).Take(100);
                return JsonConvert.SerializeObject(p.ToList());
            }
            catch (Exception ex)
            {
                return "";
            }
        }
        public int getRankingPos(int point,int levels) {
            try
            {
                int? rank = (int?)db.crosswords.Count(o => o.point > point && o.levels >= levels);
                if (rank == null) rank = 0;
                int? rank2= (int?) db.crosswords.Count(o => o.point ==point && o.levels >= levels);
                rank=rank+rank2-1;
                rank++;
                return (int)rank;
            }
            catch (Exception ex) {
                return -1;
            }
        }
        public string readPdfFile() {
            string path = "D:\\Project\\GitHub\\CrossWord\\branches\\editor\\Cau_Hoi_On_Thi_Ai_La_Trieu_Phu.pdf";
            using (PdfReader reader = new PdfReader(path))
            {
                StringBuilder text = new StringBuilder();

                for (int i = 1; i <= reader.NumberOfPages; i++)
                {
                    text.Append(PdfTextExtractor.GetTextFromPage(reader, i));
                }
                StreamWriter Sw = new StreamWriter("D:\\Project\\GitHub\\CrossWord\\branches\\editor\\word.txt");
                Sw.WriteLine(text.ToString());
                Sw.Close();
                StreamReader Sr = new StreamReader("D:\\Project\\GitHub\\CrossWord\\branches\\editor\\word.txt");
                string temp = "";
                string nomark = "";
                while ((temp = Sr.ReadLine()) != null)
                {
                    if (temp.Contains("A.") || temp.Contains("B.") || temp.Contains("C.") || temp.Contains("D.") || temp.Contains("E.")) {
                        temp = temp.Replace("A.", "");
                        temp = temp.Replace("B.", "");
                        temp = temp.Replace("C.", "");
                        temp = temp.Replace("D.", "");
                        temp = temp.Replace("E.", "");
                        nomark = OChu.unicodeToNoMark(temp).ToLowerInvariant();
                        if (nomark.Length <= 13 && nomark.Length >= 3)
                        {
                            word w = new word();
                            w.word1 = nomark;
                            w.wordvn = temp;
                            w.status = 0;
                            db.words.Add(w);
                            db.SaveChanges();
                        }
                    }
                    

                }
                //return text.ToString();
            }
            return "ok";
        }
        public class key
        {
            public string word { get; set;}
        }
        public string getListKeyword(string keyword) {
            string query = "select word from word where word like N'" + keyword + "%' order by word";
            var p=db.Database.SqlQuery<key>(query).ToList();
            return JsonConvert.SerializeObject(p);
        }
    }//Class
}
