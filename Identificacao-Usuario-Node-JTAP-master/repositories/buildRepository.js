const { db } = require("../config/firebase");

class BuildRepository{
    async getBuilds(){
        try{
            const Builds = await db.collection('builds').get();
            return Builds;
        }catch(error){
            console.error('Error getting Builds', error);
        }
    }

    async getBuildByKey(key){
        try{
            return await db.collection('builds').doc(key).get();
        }catch(error){
            console.error('Error getting Build by id', error);
        }
    }

    async createBuild(Build){
        try{
            const response = await db.collection('builds').add(Build);
            return response;
        }catch(error){
            console.error('Error creating Build', error);
        }
    }

    async updateBuild(id, Build){
        try{
            await db.collection('builds').doc(id).update(Build);
            return true;
        }catch(error){
            console.error('Error updating Build', error);
        }
    }

    async deleteBuild(id){
        try{
            await db.collection('builds').doc(id).delete();
            return true;
        }catch(error){
            console.error('Error deleting Build', error);
        }
    }
}


module.exports = new BuildRepository();