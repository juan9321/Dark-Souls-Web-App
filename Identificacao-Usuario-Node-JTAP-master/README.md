# ETAPAS
O projeto está finalizado. Contendo:

- [x] Estrutura
- [x] Front-End
- [x] Login
- [x] Cadastro

> Projeto CRUD com Firebase FireStore em Node.js e Handlebars com Autenticação do Firebase 

# INICIANDO PROJETO</h1>
### Configure seu Firebase com Firestore e Autenticação por Email e Senha
### Adicione seu arquivo .json com a chave da API Firebase no pasta src e a renomeie para 'firebase-key.json'
### Renomeie o arquivo ".env.example" para ".env" e preencha as informações segundo as credenciais do Firebase
```
git clone <link_repositorio>
cd <projeto>
npm i
cd src
node app.js
```
- htttp://localhost:5000

<br><br>

# EXPLICAÇÕES
- Server: arquivo de conexão ao banco
- repositories: arquivo para manipulação do banco
- public: arquivos JS, CSS e Imagens da aplicação
- app.js: inicialização das bibliotecas, rotas e operações
- views/layout: arquivos de layout que serão utilizados em todas as páginas
- views: paginas do projeto 
- middlewares: arquivos de "meio-campo" para validação
- controllers: arquivos de controle da aplicação
- config: arquivo de configuração do Firebase
- routes: arquivo de roteamento das rotas verificadas

<br><br>

# GITHUB
> SUBINDO SUAS ALTERAÇÕES PARA O GITHUB
## PRIMEIRA VEZ (quando ainda não existir a sua branch):
```
git checkout -b <seu_nome>
git add *
git commit -m "<seu_comentario>"
git push -u origin <seu_nome>
```
- pull request pelo site do Github da sua branch para a branch de homologação

<br>

## OUTRAS VEZES
```
git checkout -b <seu_nome>
git add *
git commit -m "<seu_comentario>"
git push origin <seu_nome>
```
- pull request pelo site do Github da sua branch para a branch de homologação

<br><br>

# PUXANDO AS ALTERAÇÕES DA BRANCH DE HOMOLOGAÇÃO
Caso o projeto já exista na sua máquina e outra pessoa tiver feito alterações antes de você, execute:
```
git pull <link_repositorio> homologacao
```

<br><br>

# SUBINDO PARA UMA BRANCH EXISTENTE
Caso você esteja editando em um PC novo e queira atualizar sua branch que JA EXISTE:
```
//Comece trazendo os dados da branch
git clone -b <sua_branch> <link_repositorio>

//Após editar tudo
git add *
git commit -m "<comentario>"
git push origin <sua_branch>
```

<br><br><br>

# DOCUMENTAÇÃO
```
npm i express --save
npm i express-handlebars --save
npm i express-session --save
npm i body-parser --save
npm i handlebars-helpers --save
npm i bootstrap --save
npm i jquery --save
npm i firebase --save
npm i firebase-admin --sava
npm i dotenv --save
npm i cookie-parser --save
npm i nodemon --save-dev
```

- Handlebars: https://handlebarsjs.com/guide/
- Bootstrap: https://getbootstrap.com
- Firebase: https://firebase.google.com/docs/firestore/query-data/get-data?hl=pt-br 
- Tutorial Helper: https://permify.co/post/firebase-authentication-nodejs/
