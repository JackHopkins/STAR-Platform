package models.users;

import java.util.Iterator;









import persistence.MongoDBManager;

import com.google.code.morphia.Key;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Query;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

@Entity(value = "token", noClassnameStored = true)
@Indexes({
    @Index(value="uuid", name="uuidIdx", unique=true),
        })
public class MongoToken{

    @Property
    public String uuid;

    @Property
    public Long creationTime;
    
    @Property
    public String email;

    @Property
    public Long expirationTime;
    
    @Property
    public Boolean isSignUp;
    
    public MongoToken() {
     
    }
    
   
    
    public static Query<MongoToken> find() {
        return (Query<MongoToken>) MongoDBManager.getInstance().dataStore.createQuery(MongoToken.class);
    }
    
    public static void drop() {
        DBCollection collection =  MongoDBManager.getInstance().dataStore.getCollection(MongoToken.class);
        collection.drop();
    }
    
    public static MongoToken createToken() {
        return new MongoToken();
    }
    
    public static int save(MongoToken entity) {
        return create(entity);
    }

    public static int create(MongoToken entity) {
        Key<MongoToken> key =  MongoDBManager.getInstance().dataStore.save(entity);
        if (null != key && null != key.getId())
            return 1;
        return 0;
    }

    public static int delete(MongoToken entity) {
        WriteResult wr = MongoDBManager.getInstance().dataStore.delete(entity);
        if (null != wr.getError())
            return 1;
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static Iterable<MongoToken> findAll() {
        Iterator<MongoToken> res = (Iterator<MongoToken>) MongoDBManager.getInstance().dataStore.createQuery(MongoToken.class)
                                                        .fetch();
        return (Iterable<MongoToken>) res;
    }        

}
