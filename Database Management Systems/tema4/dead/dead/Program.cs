using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Data.SqlClient;
using System.Threading;

namespace dead
{
    class Program
    {
        private static SqlConnection connection;
        static void Main(string[] args)
        {
            connection = new SqlConnection("Server=LAPTOPMSI24;Database = ChampionsLeague; Integrated Security = true;TrustServerCertificate=true;");
            connection.Open();

            Thread thread1 = new Thread(runThread1);
            Thread thread2 = new Thread(runThread2);

            thread1.Start();
            thread2.Start();
        }

        private static void runThread1()
        {
            Console.WriteLine("Entered in thread1");
            try
            {
                SqlCommand command = new SqlCommand("usp_run_thread1", connection);
                command.CommandType = CommandType.StoredProcedure;
                command.ExecuteNonQuery();
                Console.WriteLine("Exited in thread1");
            }
            catch (SqlException ex)
            {
                if (ex.Number == 1205)
                {
                    Console.WriteLine("Deadlock in thread1");
                }
                else
                {
                    Console.WriteLine("Error in database");
                }
            }
        }

        private static void runThread2()
        {
            try
            {
                Console.WriteLine("Entered in thread2");
                SqlCommand command = new SqlCommand("usp_run_thread2", connection);
                command.CommandType = CommandType.StoredProcedure;
                command.ExecuteNonQuery();
                Console.WriteLine("Exited in thread2");
            }
            catch (SqlException ex)
            {
                if (ex.Number == 1205)
                {
                    Console.WriteLine("Deadlock in thread2");
                }
                else
                {
                    Console.WriteLine("Error in database");
                }
            }
        }
    }
}
