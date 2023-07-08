using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics.Contracts;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.UI.WebControls;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;


using System.Configuration;
using TextBox = System.Windows.Forms.TextBox;

namespace SGBD1
{
    public partial class Form1 : Form
    {
        static string con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
        SqlConnection cs = new SqlConnection(con);
        
        SqlDataAdapter da1 = new SqlDataAdapter();
        DataSet ds1 = new DataSet();
        BindingSource bs1 = new BindingSource();

        SqlDataAdapter da2 = new SqlDataAdapter();
        DataSet ds2 = new DataSet();
        BindingSource bs2 = new BindingSource();

        private List<TextBox> textBoxList;
        DataGridViewCellEventArgs cursor1, cursor2;


        public Form1()
        {
            InitializeComponent();
        }


        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            cursor1 = e;
            string ParentTableNameID = ConfigurationManager.AppSettings["ParentTableNameID"];
            string select2 = ConfigurationSettings.AppSettings["select2"];

            int idStadion = Convert.ToInt32(dataGridView1.Rows[e.RowIndex].Cells[ParentTableNameID].Value);


            da2.SelectCommand = new SqlCommand(select2, cs);
            da2.SelectCommand.Parameters.AddWithValue("@" + ParentTableNameID, idStadion);
            ds2.Clear();
            da2.Fill(ds2);

            
            dataGridView2.DataSource = ds2.Tables[0];
            bs2.DataSource = ds2.Tables[0];

        }

        private void Display_Click(object sender, EventArgs e)
        {
            string select = ConfigurationSettings.AppSettings["select"];


            da1.SelectCommand = new SqlCommand(select, cs);
            ds1.Clear();
            da1.Fill(ds1);
            dataGridView1.DataSource = ds1.Tables[0];
            bs1.DataSource = ds1.Tables[0];
            
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            cursor2 = e;
        }

        private void button2_Click(object sender, EventArgs e)
        {

            List<string> ColumnNamesList = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));

            string update = ConfigurationSettings.AppSettings["UpdateQuery"];
            string ChildTableNameID = ConfigurationManager.AppSettings["ChildTableNameID"];

            int x;
            da2.UpdateCommand = new SqlCommand(update, cs);
            da2.UpdateCommand.Parameters.Add("@id",SqlDbType.Int).Value = dataGridView2.Rows[cursor2.RowIndex].Cells[ChildTableNameID].Value;

            for(int i = 0; i < ColumnNamesList.Count; i++)
            {
                if (string.IsNullOrWhiteSpace(textBoxList[i].Text))
                {
                   MessageBox.Show("Please fill in both fields.");
                   return;
                }

                da2.UpdateCommand.Parameters.AddWithValue("@" + ColumnNamesList[i], textBoxList[i].Text);
            }

            cs.Open();
            x = da2.UpdateCommand.ExecuteNonQuery();
            cs.Close();
            if (x >= 1)
            {
                MessageBox.Show("The record has been updated");
            }

            textBox1.Clear();
            textBox2.Clear();

        }

        private void button3_Click(object sender, EventArgs e)
        {
            List<string> ColumnNamesList = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
            string insert = ConfigurationSettings.AppSettings["InsertQuery"];
            string ParentTableNameID = ConfigurationManager.AppSettings["ParentTableNameID"];

            da2.InsertCommand = new SqlCommand(insert, cs);
            da2.InsertCommand.Parameters.Add("@id", SqlDbType.Int).Value = dataGridView1.Rows[cursor1.RowIndex].Cells[ParentTableNameID].Value;

            for (int i = 0; i < ColumnNamesList.Count; i++)
            {
                if (string.IsNullOrWhiteSpace(textBoxList[i].Text))
                {
                    MessageBox.Show("Please fill in both fields.");
                    return;
                }
                    

                da2.InsertCommand.Parameters.AddWithValue("@" + ColumnNamesList[i], textBoxList[i].Text);
            }


            cs.Open();
            da2.InsertCommand.ExecuteNonQuery();
            cs.Close();

            textBox1.Clear();
            textBox2.Clear();

            
        }

        private void button4_Click(object sender, EventArgs e)
        {
            List<string> ColumnNamesList = new List<string>(ConfigurationManager.AppSettings["ChildColumnNames"].Split(','));
            string delete = ConfigurationSettings.AppSettings["DeleteQuery"];
            string ChildTableNameID = ConfigurationManager.AppSettings["ChildTableNameID"];

            DialogResult dr;
            dr = MessageBox.Show("Are you sure?\n No undo after delete", "Confirm Deletion", MessageBoxButtons.YesNo);
            if (dr == DialogResult.Yes)
            {
                da2.DeleteCommand = new SqlCommand(delete, cs);

                da2.DeleteCommand.Parameters.Add("@id",SqlDbType.Int).Value = dataGridView2.Rows[cursor2.RowIndex].Cells[ChildTableNameID].Value;
                
                cs.Open();
                da2.DeleteCommand.ExecuteNonQuery();
                cs.Close();
                ds2.Clear();
                da2.Fill(ds2);
            }
            else
            {
                MessageBox.Show("Deletion Aborded");
            }
        }

        private void CreateAndAddTextBoxesToPanel(int numberOfTextBoxes, FlowLayoutPanel panel)
        {
            textBoxList = new List<TextBox>();

            for (int i = 0; i < numberOfTextBoxes; i++)
            {
                TextBox textBox = new TextBox();
                textBox.Name = "textBox" + i;
                textBox.Size = new Size(100, 20);

                textBoxList.Add(textBox); 
                panel.Controls.Add(textBox); 
            }
        }


        private void flowLayoutPanel1_Paint(object sender, PaintEventArgs e)
        {
            if (textBoxList == null)
            {
                string ChildNumberOfColumns = ConfigurationManager.AppSettings["ChildNumberOfColumns"];
                int nr = int.Parse(ChildNumberOfColumns);
                CreateAndAddTextBoxesToPanel(nr, flowLayoutPanel1); 
            }
        }
    }
}
