using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

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
            public string keyword;//Từ khóa
            public string question;//Câu hỏi cho từ khóa này
        }
        public static item[] Result;
        public static ques[] arrQues;
        public static int totalQues = 0;
        //Khởi tạo ô chữ: số hàng, cột, độ khó, chủ đề
        public OChu(int rows,int cols,int level=-1,int topic=-1) {
            if (totalQues == 0) {
                loadQues();
            }
        }
        public static void loadQues(){
            arrQues = new ques[100];
            totalQues = 0;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "binhminh";
            arrQues[totalQues].question = "Buổi sáng sớm tinh mơ khi mặt trời bắt đầu mọc";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "hoacuc";
            arrQues[totalQues].question = "Loài hoa màu vàng rất thông dụng, có trà chiết xuất của loài hoa này.";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "dabong";
            arrQues[totalQues].question = "Hành động thông dụng tác động vào quả bóng";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "damphan";
            arrQues[totalQues].question = "Thương lượng, hội đàm trong chính trị, kinh doanh";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "nganhang";
            arrQues[totalQues].question = "Nhà bank, nơi gửi tiền";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "imlang";
            arrQues[totalQues].question = "Không có âm thanh!";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "hoamai";
            arrQues[totalQues].question = "Hoa vào dịp Tết người Nam hay dùng";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "messi";
            arrQues[totalQues].question = "Cầu thủ 4 lần liên tiếp đạt quả bóng vàng thế giới";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "ronaldo";
            arrQues[totalQues].question = "Cầu thủ đạt quả bóng vàng thế giới năm 2014";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "duc";
            arrQues[totalQues].question = "Đội bóng vô địch thế giới năm 2014";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "hochiminh";
            arrQues[totalQues].question = "Tên gọi hiện nay của Sài Gòn. TP...";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "tranhungdao";
            arrQues[totalQues].question = "Ai là người viết Hịch tướng sỹ?";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "leloi";
            arrQues[totalQues].question = "Vị vua khởi nghĩa Lam Sơn chiến thắng quân Minh.";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "nguyentrai";
            arrQues[totalQues].question = "Người viết Bình Ngô Đại Cáo";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "ngoctrinh";
            arrQues[totalQues].question = "Người mẫu nội y nổi tiếng nhất Việt Nam";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "truongngocanh";
            arrQues[totalQues].question = "nữ diễn viên đóng phim Áo lụa hà đông";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "duonglam";
            arrQues[totalQues].question = "Vua Phùng Hưng và Ngô Quyền đều sinh ra tại làng này thuộc tỉnh Hà Tây.";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "vothithang";
            arrQues[totalQues].question = "Chiến sĩ biệt động thành với nụ cười chiến thắng";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "lyconguan";
            arrQues[totalQues].question = "Vị vua cho dời đô về Thăng Long năm 1010";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "nguyenhien";
            arrQues[totalQues].question = "Vị trạng nguyên trẻ tuổi nhất trong lịch sử Việt Nam";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "batrieu";
            arrQues[totalQues].question = "Nữ anh hùng dân tộc người Thanh Hóa, chống quân Ngô";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "lythuongkiet";
            arrQues[totalQues].question = "Thái úy đời Lý đánh bại quân Tống năm 1075-1077";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "haibatrung";
            arrQues[totalQues].question = "Hai nữ anh hùng dân tộc chống lại quân Hán.";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "sapa";
            arrQues[totalQues].question = "Là một thị trấn vùng cao, là một khu nghỉ mát nổi tiếng thuộc tỉnh Lào Cai";
            totalQues++;
            arrQues[totalQues] = new ques();
            arrQues[totalQues].keyword = "halong";
            arrQues[totalQues].question = "Vịnh rồng bay, một trong các kỳ quan thiên nhiên thế giới và của Việt Nam.";
            totalQues++;
            

            Array.Resize(ref arrQues, totalQues);
        }
        //public static bool canPlace(string x,string str)//have x in str
        public static item[] getOChu()
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
            while (fromIndex <= 0)
            {
                i=rnd.Next(0, totalQues);
                mainIndex = i;
                mainKeyword = arrQues[i].keyword;
                mainQues = arrQues[i].question;
                maxRight = -1;
                maxLeft = -1;
                allWord = ""; 
                while (i < totalQues)
                {
                    i = i + 1;
                    if (i >= totalQues)
                    {
                        i = 0;
                        continue;
                    }
                    if (!allWord.Contains(","+arrQues[i].keyword + ",") && arrQues[i].keyword.Contains(mainKeyword[fromIndex] + ""))
                    {
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
                        i = mainIndex + 1;
                        count++;                        
                    }
                    if (fromIndex >= mainKeyword.Length - 1) break;
                    if (i == mainIndex) break;
                }
            }
            fromIndex = 0;
            for (int j = 0; j < count; j++) {
                if (Result[j].keyword.Contains(mainKeyword[fromIndex] + ""))
                {

                    //int right = Result[j].keyword.Length - Result[j].keyword.IndexOf(mainKeyword[fromIndex] + "");
                    int left = Result[j].keyword.IndexOf(mainKeyword[fromIndex] + "");
                    
                    Result[j].fromX = fromIndex;
                    Result[j].fromY = maxLeft-left;
                    Result[j].toX = fromIndex;
                    Result[j].toY = Result[j].fromY + Result[j].keyword.Length - 1;
                    fromIndex++;
                }
            }
            Result[0].rows = mainKeyword.Length;
            Result[0].cols = maxLeft+maxRight;
            //count++;
            Result[count] = new item();
            Result[count].keyword = mainKeyword;
            Result[count].question = mainQues;
            Result[count].fromX = 0;
            Result[count].fromY = maxLeft;
            Result[count].toX = mainKeyword.Length-1;
            Result[count].toY = maxLeft;
            Result[count].type = 1;
            count++;
                //for (int i = 0; i < 10; i++)
                //{
                //Result[count]=new item();
                //Result[count].keyword = "binhminh";
                //Result[count].question = "buổi sáng sớm tinh mơ khi mặt trời bắt đầu mọc";
                //Result[count].fromX = 0;
                //Result[count].fromY = 0;
                //Result[count].toX = 0;
                //Result[count].toY = 7;
                //Result[count].rows =6;
                //Result[count].cols = 9;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "hoacuc";
                //Result[count].question = "loài hoa màu vàng rất thông dụng, có trà chiết xuất của loài hoa này.";
                //Result[count].fromX = 1;
                //Result[count].fromY = 2;
                //Result[count].toX = 1;
                //Result[count].toY = 7;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "dabong";
                //Result[count].question = "hành động thông dụng tác động vào quả bóng";
                //Result[count].fromX = 2;
                //Result[count].fromY = 2;
                //Result[count].toX = 2;
                //Result[count].toY = 7;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "damphan";
                //Result[count].question = "thương lượng, hội đàm trong chính trị, kinh doanh";
                //Result[count].fromX = 3;
                //Result[count].fromY = 1;
                //Result[count].toX = 3;
                //Result[count].toY = 7;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "nganhang";
                //Result[count].question = "nhà bank, nơi gửi tiền";
                //Result[count].fromX = 4;
                //Result[count].fromY = 1;
                //Result[count].toX = 4;
                //Result[count].toY = 8;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "imlang";
                //Result[count].question = "không có âm thanh!";
                //Result[count].fromX = 5;
                //Result[count].fromY = 3;
                //Result[count].toX = 5;
                //Result[count].toY = 8;
                //count++;
                //Result[count] = new item();
                //Result[count].keyword = "hoamai";
                //Result[count].question = "hoa vào dịp Tết";
                //Result[count].fromX = 0;
                //Result[count].fromY = 3;
                //Result[count].toX = 5;
                //Result[count].toY = 3;
                //Result[count].type = 1;
                //count++; 

                //}
                Array.Resize(ref Result, count);
            return Result;
        }
       
    }
}