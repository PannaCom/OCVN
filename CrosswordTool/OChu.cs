using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using CrosswordTool.Models;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.IO;
namespace CrosswordTool
{
    class OChu
    {
        public static string[,] oc = new string[3, 3] {{"a","b","c"},
                                      {"d","e","f"}, 
                                      {"g","h","i"} };
        public class item {
            public string keyword;//Từ khóa
            public string question;//Câu hỏi cho từ khóa này
            public int fromX;//tọa độ bắt đầu
            public int fromY;
            public int toX;//tọa độ kết thúc
            public int toY;
            public int type=0;//=0 ngang,=1 dọc
            public string topic;//chủ đề
            public int rows;
            public int cols;
        }
        public class ques {
            public string keyword{get;set;}//Từ khóa
            public string question{get;set;}//Câu hỏi cho từ khóa này
            public bool show{get;set;}
        }
        public static item[] Result;
        public static ques[] arrQues;
        public static int totalQues = 0;
        public static int Rows = 10;
        public static int Cols = 10;
        //Khởi tạo ô chữ: số hàng, cột, độ khó, chủ đề
        private static binhyenEntities db = new binhyenEntities();
        public OChu(int rows,int cols,int level=-1,int topic=-1) {
            Rows = rows;
            Cols = cols;
            totalQues = 0;
            if (totalQues == 0) {
                loadQues();
            }
        }
        public static void loadQues(){
            arrQues = new ques[1400];
            totalQues = 0;
            var p = (from q in db.words select q).OrderBy(o => o.word1).Distinct().ToList();
            for (int i = 0; i < p.Count; i++)
            {
                arrQues[i] = new ques();
                arrQues[i].keyword = p[i].word1;
                arrQues[i].question = "";
                arrQues[i].show = true;
                totalQues++;
            }
            Array.Resize(ref arrQues, totalQues);
        }
        public static bool isFit(int cols, string keyword, string character) {
            int from = keyword.IndexOf(character);
            int lengthright = keyword.Length - from;
            int lengthleft = from;
            if (lengthright <= cols / 2 + 1 && lengthleft <= cols / 2 + 1) return true;
            return false;
        }
        public static int getIndexFirst(int totalQues)
        {
            for (int jj = 0; jj < totalQues; jj++) {
                if (arrQues[jj].show && arrQues[jj].keyword.Length <= Cols && arrQues[jj].keyword.Length>=5) return jj;
            }
            return -1;
        }
        //public static bool canPlace(string x,string str)//have x in str
        public void getOChu()
        {
            //string result = "";
            Result = new item[100];
            //for (int k = 0; k <= 100; k++) { 

            //}
            int count = 0;
            Random rnd = new Random();
            int i = 0;
            //Chọn i là ô chữ chính
            int maxRight = -1;
            int maxLeft = -1;
            string mainKeyword = arrQues[i].keyword;
            string mainQues = arrQues[i].question;
            int fromIndex=0;
            int mainIndex = i;
            string allWord = ""; //mainKeyword + ",";
            int totalCount = 0;
            while (fromIndex <= 0)
            {
                i=getIndexFirst(totalQues);// Lấy ngẫu nhiên một từ khóa
                if (i <= -1) break;
                mainIndex = i;
                mainKeyword = arrQues[i].keyword;
                mainQues = arrQues[i].question;
                maxRight = -1;
                maxLeft = -1;
                allWord = "";
                string listWordIndex = "";
                i = 0;
                while (i < totalQues)
                {
                    i = i + 1;
                    if (i >= totalQues)
                    {
                        //Khởi tạo lại từ đầu:
                        count = 0;
                        i = 0;
                        arrQues[mainIndex].show = false;
                        //Chọn i là ô chữ chính
                        maxRight = -1;
                        maxLeft = -1;
                        mainKeyword = arrQues[i].keyword;
                        mainQues = arrQues[i].question;
                        fromIndex = 0;
                        mainIndex = i;
                        allWord = ""; //mainKeyword + ",";
                        listWordIndex = "";
                        break;
                    }
                    //Nếu từ khóa i này có chứa 1 ký tự thứ fromindex(tăng dần), Thì ta có thể nhét nó vào.
                    if (arrQues[i].show && !allWord.Contains("," + arrQues[i].keyword + ",") && arrQues[i].keyword.Contains(mainKeyword[fromIndex] + "") && isFit(Cols, arrQues[i].keyword, mainKeyword[fromIndex] + "")) 
                    {
                        //arrQues[i].show = false;
                        listWordIndex += "," + i + ",";
                        allWord += "," + arrQues[i].keyword + ",";
                        Result[count] = new item();
                        Result[count].keyword = arrQues[i].keyword;
                        Result[count].question = arrQues[i].question;
                        Result[count].type = 0;
                        int right = arrQues[i].keyword.Length - arrQues[i].keyword.IndexOf(mainKeyword[fromIndex] + "");
                        int left = arrQues[i].keyword.IndexOf(mainKeyword[fromIndex] + "");
                        if (right > maxRight) maxRight = right;
                        if (left > maxLeft) maxLeft = left;
                        fromIndex++;
                        //i = mainIndex + 1;
                        count++;                        
                    }
                    if (fromIndex >= mainKeyword.Length)
                    {
                        for (int l = mainIndex; l <= i; l++) {
                            if (listWordIndex.IndexOf("," + l + ",")>=0) arrQues[l].show = false;
                        }
                        //Ghi ra ô chữ
                        totalCount++;
                        StreamWriter SW = new StreamWriter("D:\\Project\\GitHub\\CrossWord\\branches\\editor\\OchuRaw\\" + totalCount + ".txt");
                        SW.WriteLine("Ô chữ chính: " + mainKeyword);
                        for (int kk = 0; kk < count; kk++) {
                            SW.WriteLine("Ô "+kk+":" + Result[kk].keyword);
                        }
                        SW.Close();
                        arrQues[mainIndex].show = false;
                        //Khởi tạo lại từ đầu:
                        count = 0;
                        i = 0;
                        //Chọn i là ô chữ chính
                        maxRight = -1;
                        maxLeft = -1;
                        mainKeyword = arrQues[i].keyword;
                        mainQues = arrQues[i].question;
                        fromIndex = 0;
                        mainIndex = i;
                        allWord = ""; //mainKeyword + ",";
                        listWordIndex = "";
                        break;
                    }
                    //if (i == mainIndex) break;
                }
            }
            //fromIndex = 0;
            //for (int j = 0; j < count; j++) {
            //    if (Result[j].keyword.Contains(mainKeyword[fromIndex] + ""))
            //    {

            //        //int right = Result[j].keyword.Length - Result[j].keyword.IndexOf(mainKeyword[fromIndex] + "");
            //        int left = Result[j].keyword.IndexOf(mainKeyword[fromIndex] + "");
                    
            //        Result[j].fromX = fromIndex;
            //        Result[j].fromY = maxLeft-left;
            //        Result[j].toX = fromIndex;
            //        Result[j].toY = Result[j].fromY + Result[j].keyword.Length - 1;
            //        fromIndex++;
            //    }
            //}
            //Result[0].rows = mainKeyword.Length;
            //Result[0].cols = maxLeft+maxRight;
            ////count++;
            //Result[count] = new item();
            //Result[count].keyword = mainKeyword;
            //Result[count].question = mainQues;
            //Result[count].fromX = 0;
            //Result[count].fromY = maxLeft;
            //Result[count].toX = mainKeyword.Length-1;
            //Result[count].toY = maxLeft;
            //Result[count].type = 1;
            //count++;
                
            //    Array.Resize(ref Result, count);
            //return Result;
        }
        //convert tieng viet thanh khong dau va them dau -
        public static string unicodeToNoMark(string input)
        {
            input = input.ToLowerInvariant().Trim();
            if (input == null) return "";
            string noMark = "a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,e,e,e,e,e,e,e,e,e,e,e,e,u,u,u,u,u,u,u,u,u,u,u,u,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,i,i,i,i,i,i,y,y,y,y,y,y,d,A,A,E,U,O,O,D";
            string unicode = "a,á,à,ả,ã,ạ,â,ấ,ầ,ẩ,ẫ,ậ,ă,ắ,ằ,ẳ,ẵ,ặ,e,é,è,ẻ,ẽ,ẹ,ê,ế,ề,ể,ễ,ệ,u,ú,ù,ủ,ũ,ụ,ư,ứ,ừ,ử,ữ,ự,o,ó,ò,ỏ,õ,ọ,ơ,ớ,ờ,ở,ỡ,ợ,ô,ố,ồ,ổ,ỗ,ộ,i,í,ì,ỉ,ĩ,ị,y,ý,ỳ,ỷ,ỹ,ỵ,đ,Â,Ă,Ê,Ư,Ơ,Ô,Đ";
            string[] a_n = noMark.Split(',');
            string[] a_u = unicode.Split(',');
            for (int i = 0; i < a_n.Length; i++)
            {
                input = input.Replace(a_u[i], a_n[i]);
            }
            input = input.Replace("  ", " ");
            input = Regex.Replace(input, "[^a-zA-Z0-9% ._]", string.Empty);
            input = removeSpecialChar(input);
            input = input.Replace(" ", "-");
            input = input.Replace("--", "-");
            return input;
        }
        public static string removeSpecialChar(string input)
        {
            input = input.Replace("-", "").Replace(":", "").Replace(",", "").Replace("_", "").Replace("'", "").Replace("\"", "").Replace(";", "").Replace("”", "").Replace(".", "").Replace("%", "");
            return input;
        }
    }//Class
}