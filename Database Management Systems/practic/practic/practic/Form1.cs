using Microsoft.Data.SqlClient;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace practic
{
    public partial class Form1 : Form
    {

        SqlConnection cs = new SqlConnection("Server=LAPTOPMSI24;Database = S5; Integrated Security = true;TrustServerCertificate=true;");
        SqlDataAdapter da1 = new SqlDataAdapter();
        DataSet ds1 = new DataSet();
        BindingSource bs1 = new BindingSource();

        SqlDataAdapter da2 = new SqlDataAdapter();
        DataSet ds2 = new DataSet();
        BindingSource bs2 = new BindingSource();

        int idt = 0;
        int ida = 0;

        public Form1()
        {
            InitializeComponent();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            int idTip = Convert.ToInt32(dataGridView1.Rows[e.RowIndex].Cells["Tid"].Value);
            idt = idTip;


            da2.SelectCommand = new SqlCommand("SELECT * FROM Articole WHERE Tid = @Tid;", cs);
            da2.SelectCommand.Parameters.AddWithValue("@Tid", idTip);
            ds2.Clear();
            da2.Fill(ds2);


            dataGridView2.DataSource = ds2.Tables[0];
            bs2.DataSource = ds2.Tables[0];
        }

        private void button1_Click(object sender, EventArgs e)
        {
            da1.SelectCommand = new SqlCommand("SELECT * FROM " + "TipuriArticole;", cs);
            ds1.Clear();
            da1.Fill(ds1);
            dataGridView1.DataSource = ds1.Tables[0];
            bs1.DataSource = ds1.Tables[0];
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(textBox1.Text) || string.IsNullOrWhiteSpace(textBox2.Text) || string.IsNullOrWhiteSpace(textBox3.Text) || string.IsNullOrWhiteSpace(textBox4.Text))
            {
                MessageBox.Show("Please fill all fields.");
                return;
            }


            int n;
            bool isNumeric = int.TryParse(textBox1.Text, out n);
            if (isNumeric)
            {
                MessageBox.Show("The first field can only contain a word.");
                return;
            }


            int x;
            da2.UpdateCommand = new SqlCommand("Update Articole set Titlu = @t, NrAutori = @a , NrPagini = @p , AnPublicare = @pub where Aid = @id", cs);

            da2.UpdateCommand.Parameters.Add("@t", SqlDbType.VarChar).Value = textBox1.Text;
            da2.UpdateCommand.Parameters.Add("@a", SqlDbType.Int).Value = textBox2.Text;
            da2.UpdateCommand.Parameters.Add("@p", SqlDbType.Int).Value = textBox3.Text;
            da2.UpdateCommand.Parameters.Add("@pub", SqlDbType.Int).Value = textBox4.Text;
            da2.UpdateCommand.Parameters.Add("@id", SqlDbType.Int).Value = ida;

            cs.Open();
            x = da2.UpdateCommand.ExecuteNonQuery();
            cs.Close();
            if (x >= 1)
            {
                MessageBox.Show("The record has been updated");
            }

            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
        }

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            ida = Convert.ToInt32(dataGridView2.Rows[e.RowIndex].Cells["Aid"].Value);
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(textBox1.Text) || string.IsNullOrWhiteSpace(textBox2.Text) || string.IsNullOrWhiteSpace(textBox3.Text) || string.IsNullOrWhiteSpace(textBox4.Text))
            {
                MessageBox.Show("Please fill all fields.");
                return;
            }


            int n;
            bool isNumeric = int.TryParse(textBox1.Text, out n);
            if (isNumeric)
            {
                MessageBox.Show("The first field can only contain a word.");
                return;
            }


            da2.InsertCommand = new SqlCommand("INSERT INTO Articole VALUES (@t, @a, @p, @pub, @id)", cs);
            da2.InsertCommand.Parameters.Add("@t", SqlDbType.VarChar).Value = textBox1.Text;
            da2.InsertCommand.Parameters.Add("@a", SqlDbType.Int).Value = textBox2.Text;
            da2.InsertCommand.Parameters.Add("@p", SqlDbType.Int).Value = textBox3.Text;
            da2.InsertCommand.Parameters.Add("@pub", SqlDbType.Int).Value= textBox4.Text;
            da2.InsertCommand.Parameters.Add("@id", SqlDbType.Int).Value = idt;
            cs.Open();
            da2.InsertCommand.ExecuteNonQuery();
            cs.Close();

            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            DialogResult dr;
            dr = MessageBox.Show("Are you sure?\n No undo after delete", "Confirm Deletion", MessageBoxButtons.YesNo);
            if (dr == DialogResult.Yes)
            {
                da2.DeleteCommand = new SqlCommand("Delete from Articole where Aid = @id", cs);

                da2.DeleteCommand.Parameters.Add("@id", SqlDbType.Int).Value = ida;
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
    }
}
