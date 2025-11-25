# Insérer des données de test dans la base de données

## Problème
Si les produits n'apparaissent pas sur la page, c'est probablement parce que la base de données est vide.

## Solution : Insérer des données de test

### Option 1 : Utiliser le script SQL (Recommandé)

1. **Connectez-vous à MySQL** :
   ```bash
   mysql -u root -p
   ```

2. **Exécutez le script** :
   ```bash
   mysql -u root -p phone_shoop < insert_sample_data.sql
   ```
   
   Ou depuis MySQL :
   ```sql
   USE phone_shoop;
   SOURCE insert_sample_data.sql;
   ```

3. **Vérifiez les données** :
   ```sql
   SELECT COUNT(*) FROM Product;
   SELECT COUNT(*) FROM Category;
   ```

### Option 2 : Utiliser l'interface Admin

1. **Créer un utilisateur admin** :
   - Allez sur `/register.jsp`
   - Créez un compte
   - Connectez-vous à MySQL et modifiez le type d'utilisateur :
     ```sql
     USE phone_shoop;
     UPDATE User SET user_type = 'admin' WHERE user_email = 'votre_email@example.com';
     ```

2. **Se connecter en tant qu'admin** :
   - Allez sur `/login.jsp`
   - Connectez-vous avec votre compte admin

3. **Ajouter des catégories** :
   - Allez sur `/admin.jsp`
   - Cliquez sur "Add Category"
   - Ajoutez des catégories (ex: Smartphones, Tablets, Accessories)

4. **Ajouter des produits** :
   - Sur la page admin, cliquez sur "Add Product"
   - Remplissez le formulaire avec les détails du produit
   - **Important** : Vous devez avoir des images dans le dossier `src/main/webapp/img/products/`
   - Les images seront uploadées automatiquement lors de l'ajout du produit

### Option 3 : Insérer manuellement via SQL

```sql
USE phone_shoop;

-- 1. Insérer une catégorie
INSERT INTO Category (categoryTitle, categoryDescription) 
VALUES ('Smartphones', 'Latest smartphones with advanced features');

-- 2. Récupérer l'ID de la catégorie (notez l'ID retourné)
SELECT categoryId FROM Category WHERE categoryTitle = 'Smartphones';

-- 3. Insérer un produit (remplacez 1 par l'ID de la catégorie)
INSERT INTO Product (pName, pDesc, pPrice, pDiscount, pQuantity, pPhoto, category_categoryId) 
VALUES ('iPhone 15', 'Latest iPhone model', 999, 10, 50, 'iphone15.png', 1);
```

## Vérification

Après avoir inséré les données, vérifiez que tout fonctionne :

1. **Redémarrez l'application** :
   ```bash
   mvn jetty:run
   ```

2. **Visitez la page d'accueil** :
   - Allez sur `http://localhost:8080/`
   - Les produits devraient maintenant apparaître

3. **Vérifiez les logs** :
   - Si vous voyez des erreurs dans les logs, vérifiez :
     - Que MySQL est démarré
     - Que la base de données `phone_shoop` existe
     - Que les tables ont été créées (Hibernate les crée automatiquement)

## Notes importantes

- **Images** : Les produits nécessitent des images. Placez les images dans `src/main/webapp/img/products/` ou utilisez des noms d'images qui existent déjà dans ce dossier.
- **Catégories** : Les produits doivent avoir une catégorie associée. Créez d'abord des catégories avant d'ajouter des produits.
- **Hibernate** : Avec `hbm2ddl.auto=update`, Hibernate créera automatiquement les tables si elles n'existent pas.

## Dépannage

### Les produits ne s'affichent toujours pas

1. **Vérifiez la console du serveur** pour des erreurs
2. **Vérifiez la base de données** :
   ```sql
   SELECT * FROM Product;
   SELECT * FROM Category;
   ```
3. **Vérifiez les logs Hibernate** : Les requêtes SQL devraient apparaître dans les logs si `show_sql=true` dans `hibernate.cfg.xml`

### Erreur "No products found"

- La base de données est vide → Insérez des données avec le script SQL
- Les catégories n'existent pas → Créez d'abord des catégories
- Problème de connexion → Vérifiez la configuration MySQL

