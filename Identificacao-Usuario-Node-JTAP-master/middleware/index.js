const { admin } = require("../config/firebase");

const verifyToken = async (req, res, next) => {
    const idToken = req.cookies.access_token;
    
    // Verificar se o token não existe
    if (!idToken) {
        return res.redirect('/login');  // Redireciona para a página de login
    }

    try {
        // Verificar o ID Token
        const decodedToken = await admin.auth().verifyIdToken(idToken);
        req.user = decodedToken;  // Salva os dados do usuário no req.user
        next();  // Continua para o próximo middleware ou rota
    } catch (error) {
        console.error('Error verifying token:', error);

        // Se o token expirou, remova o cookie e redirecione para login
        if (error.code === 'auth/id-token-expired') {
            res.clearCookie('access_token');  // Limpa o cookie do token expirado
            return res.redirect('/login');  // Redireciona para a página de login
        }

        // Tratar outros tipos de erro
        return res.render("errors/error", { 
            layout: "guest", 
            codError: "403", 
            textError: 'Não Autorizado!' 
        });
    }
};

module.exports = verifyToken;
