const BuildRepository = require('../repositories/buildRepository.js');

class BuildController {
    async dashboard(req, res){
        var Builds = await BuildRepository.getBuilds();
        var Builds_list = [];

        Builds.forEach(doc => {
            var key = doc.id;
            var data = doc.data();
            data.key = key;
            Builds_list.push(data);
        })

        res.render('dashboard', { Builds: Builds_list});
    }

    profile(req, res){
        res.render('auth/profile', { user: req.user });
    }

    async readBuild(req, res){
        var build = await BuildRepository.getBuildByKey(req.params.key);
        res.render('read', { build: build.data() });
    }

    createBuild(req, res){
        res.render('register');
    }

    async storeBuild(req, res){
        try{
            const Build = req.body;
            await BuildRepository.createBuild(Build);
            req.session.success = 'Build Cadastrada com Sucesso!';
        }
        catch(e){
            req.session.error = 'Erro ao Cadastrar Build!';
            console.error(e);
        }

        res.redirect('/dashboard');
    }
}


module.exports = new BuildController();