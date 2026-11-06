# Online Shopping and Order Management System

Java 11 + JSP/Servlets (`javax.servlet`) + MySQL 8 + Tomcat 9 Maven WAR starter.

## Requirements
- JDK 11
- Apache Tomcat 9
- MySQL 8.x
- Maven 3.8+
- Git; Docker Desktop (optional for container build)

## Database setup
1. Open MySQL Workbench.
2. Run `database/online_shop.sql`.
3. Create a restricted database user, for example:
   `CREATE USER 'shop_user'@'localhost' IDENTIFIED BY 'your-password';`
   `GRANT SELECT,INSERT,UPDATE,DELETE ON online_shop.* TO 'shop_user'@'localhost';`
4. Set environment variables before starting Tomcat:
   - `DB_URL=jdbc:mysql://localhost:3306/online_shop?serverTimezone=UTC`
   - `DB_USER=shop_user`
   - `DB_PASSWORD=your-password`
   If environment variables are not set, DBConnection uses local demo defaults. Change them before use.

## Build and run
```bash
mvn clean package
```
Copy `target/OnlineShopping.war` into Tomcat's `webapps` folder, start Tomcat, then open:
`http://localhost:8080/OnlineShopping/`

## Included starter features
- Customer registration and BCrypt password hashing
- Customer login/logout
- Product catalogue and search
- Session-based cart add/update/remove
- Checkout with database transaction and stock check
- Customer order history
- Basic admin product list/add page (protect admin role before classroom demo)

## Important
This is an educational starter project. Review and test all flows before submission. Add CSRF protection, stronger validation, admin-user provisioning, audit logging and production secret management before any real deployment. Never publish real credentials.
