package application;

import db.DB;
import db.DbException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner sc = new Scanner(System.in);
        Connection con = null;
        PreparedStatement pstmt = null;

        System.out.print("Deseja inserir dados no banco (s/n)? ");
        char input = sc.next().charAt(0);

        try {
            con = DB.getConnection();
            con.setAutoCommit(false);

            if (input == 's') {
                System.out.print("Deseja incluir vendedor ou departamento? ");
                String input2 = sc.next();

                if (input2.equalsIgnoreCase("vendedor")) {
                    pstmt = con.prepareStatement(
                            "INSERT INTO seller " +
                                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                                    "VALUES (?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);

                    System.out.print("Nome: ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    System.out.print("E-mail: ");
                    String email = sc.nextLine();
                    System.out.print("Data de nascimento (dd/MM/aaaa): ");
                    LocalDate birthDate = LocalDate.parse(sc.next(), fmt);
                    System.out.print("Salário base: ");
                    double baseSalary = sc.nextDouble();
                    System.out.print("ID do departamento: ");
                    int departmentId = sc.nextInt();

                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setDate(3, new Date(sdf.parse(birthDate.format(fmt)).getTime()));
                    pstmt.setDouble(4, baseSalary);
                    pstmt.setInt(5, departmentId);

                    int affectedRows = pstmt.executeUpdate();

                    System.out.println();
                    System.out.println(affectedRows + " vendedor(es) adicionado(s)");
                }
                else if (input2.equalsIgnoreCase("departamento")) {
                    pstmt = con.prepareStatement(
                            "INSERT INTO department (Name) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS);

                    System.out.print("Nome do departamento: ");
                    sc.nextLine();
                    String name = sc.nextLine();

                    pstmt.setString(1, name);

                    int affectedRows = pstmt.executeUpdate();

                    System.out.println();
                    System.out.println(affectedRows + " departamento(s) adicionado(s)");
                }
                else {
                    System.out.println("Opção inválida!");
                }
            }

            System.out.println();
            DB.printDataRetrieved();

            System.out.print("Gostaria de (a)tualizar o salário dos vendedores ou (d)eletar um? ");
            input = sc.next().charAt(0);
            if (input == 'a') {
                pstmt = con.prepareStatement("UPDATE seller SET BaseSalary = BaseSalary * ?");

                System.out.print("Porcentagem: ");
                double percentage = (sc.nextDouble() / 100)+ 1;

                pstmt.setDouble(1, percentage);

                int affectedRows = pstmt.executeUpdate();
                System.out.println();
                System.out.println(affectedRows + " salários aumentados");
                DB.printDataRetrieved();
            }
            else if (input == 'd') {
                pstmt = con.prepareStatement("DELETE FROM seller WHERE ID = ?");

                System.out.print("Digite o id do vendedor que deseja apgar do banco: ");
                int id = sc.nextInt();

                pstmt.setInt(1, id);

                int affectedRows = pstmt.executeUpdate();
                System.out.println();
                System.out.println(affectedRows + " vendedor(es) apagado(s).");
                System.out.println("Nova lista de funcionários:");
                DB.printDataRetrieved();
            }
            else {
                System.out.println("Opção inválidas!");
            }

            con.commit();

        } catch (SQLException | ParseException e) {
            try {
                con.rollback();
                throw new DbException("Transação não efetuada por erro:" + e.getMessage());
            } catch (SQLException e1) {
                throw new DbException("Erro ao realizar o rollback: " + e1.getMessage());
            }
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeConnection();
        }
    }
}
