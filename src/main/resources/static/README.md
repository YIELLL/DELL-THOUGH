# ecommerce-interface-lab
Code by De Asis, Keniel Drew D. / De Leon, Kent Jeanne S.  | Dell-Though is a multi-page E-commerce Web Application built with HTML5 to showcase flagship audio technology. Developed as a practical application of BSIT principles, the project demonstrates a complete user journey through a clean, semantic document structure.

## Security Integration

This frontend integrates with a Spring Security backend for session-based authentication:

- **Login**: `login.html` submits credentials to `/login` with CSRF token.
- **Registration**: `sign.html` posts to `/api/v1/auth/register`.
- **Session Handling**: Cookies are used for session management.
- **Error Handling**: 401 redirects to login, 403 shows access denied.
- **Dynamic Products**: Fetches product data from `/api/v1/products`.
