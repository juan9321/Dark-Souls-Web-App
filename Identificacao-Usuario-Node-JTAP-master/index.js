
//Iniciando Bibliotecas
const express = require('express');
const router = require("./routes");
const session = require('express-session');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const handlebars = require("express-handlebars").engine;
const helpers = require("handlebars-helpers")();

require("dotenv").config();
const PORT = process.env.PORT || 8080;

const app = express();

//Definindo Atalhos
app.use('/bootstrap', express.static(__dirname + '/node_modules/bootstrap/dist'));
app.use('/public', express.static('public'));
app.use('/jquery', express.static(__dirname + '/node_modules/jquery/dist'));

//Definindo layout das páginas
app.engine("handlebars", handlebars({ defaultLayout: "main", helpers: helpers }));
app.set("view engine", "handlebars");

//Iniciando Middleware que recolhe os dados do formulário via POST
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

//Iniciando Sessions
app.use(session({
    secret: 'key',
    resave: true,
    saveUninitialized: true
}));

// Middleware para passar informações do usuário para as views
app.use((req, res, next) => {
    res.locals.user = req.session.user;
    next();
});

//Iniciando Cookies Auth e Router
app.use(express.json());
app.use(cookieParser());
app.use(router);



function isLogged(req){
    const idToken = req.cookies.access_token;

    if (!idToken) {
        return false;
    }
    else{
        return true;
    }
}


function deslogar(req, res){
    //destruir cookies
    res.clearCookie('access_token');
    req.session.user = null;
    return res.redirect("/login");
}


//ROTAS
//INDEX


app.get('/', (req, res) => {
    try {
        if (isLogged(req)) {
            return res.redirect("/dashboard");
        } else {
            return deslogar(req, res);  // Passando req e res corretamente
        }
    } catch (error) {
        console.log('Erro ao Redirecionar: ', error);
        return res.render("errors/error", { layout: "guest", codError: "500", textError: 'Erro no Servidor!' });
    }
});




//LOGIN
app.get('/login', (req, res) => {
    try{
        if(isLogged(req)){
            return res.redirect("/dashboard");
        }

        return res.render("auth/login", {layout: "guest"});
    }catch(error){
        console.log('Erro ao Abrir Login: ', error);
        return res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
    }
});



//CADASTRO
app.get('/register', (req, res) => {
    try{
        if(isLogged(req)){
            return res.redirect("/dashboard");
        }

        return res.render("auth/register", {layout: "guest"});
    }catch(error){
        console.log('Erro ao Abrir Cadastro: ', error);
        return res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
    }
});



//Verificar Erro
app.get('/getError', (req, res) => {
    const error = req.session.error;
    delete req.session.error;

    if (error) {
        res.json({ error });
    } else {
        res.json({});
    }
});



//Verificar Sucesso
app.get('/getSuccess', (req, res) => {
    const success = req.session.success;
    delete req.session.success;

    if (success) {
        res.json({ success });
    } else {
        res.json({});
    }
});



//FALLBACK
app.use((req, res) => {
    return res.render("errors/error", {layout: "guest", codError: "404", textError: 'Página Não Encontrada!'});
})



//Iniciando Servidor
app.listen(PORT, () => {
    console.log(`Servidor Ativo na Porta ${PORT}!`);
});