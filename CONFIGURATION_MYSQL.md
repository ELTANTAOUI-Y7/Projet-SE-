# Configuration MySQL pour Phone Shoop

## Étapes de configuration

### 1. Vérifier que MySQL est installé et démarré

#### Sur Windows :
- Ouvrez les **Services Windows** (Win + R, tapez `services.msc`)
- Cherchez le service **MySQL** ou **MySQL80** (selon votre version)
- Assurez-vous qu'il est **démarré** (Running)

#### Alternative - Ligne de commande :
```powershell
# Vérifier si MySQL est en cours d'exécution
Get-Service | Where-Object {$_.Name -like "*mysql*"}
```

### 2. Se connecter à MySQL

Ouvrez une invite de commande ou PowerShell et connectez-vous à MySQL :

```bash
mysql -u root -p
```

Si MySQL n'est pas dans votre PATH, utilisez le chemin complet :
```bash
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p
```

### 3. Créer la base de données

Une fois connecté à MySQL, exécutez :

```sql
CREATE DATABASE IF NOT EXISTS phone_shoop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Ou utilisez le script fourni :
```bash
mysql -u root -p < setup_database.sql
```

### 4. Vérifier la configuration dans hibernate.cfg.xml

Le fichier `src/main/resources/hibernate.cfg.xml` contient la configuration de connexion :

```xml
<property name="connection.url">jdbc:mysql://localhost:3306/phone_shoop</property>
<property name="connection.username">root</property>
<property name="connection.password">root</property>
```

**Important :** Si votre mot de passe MySQL n'est pas `root`, modifiez-le dans le fichier `hibernate.cfg.xml`.

### 5. Vérifier que le port MySQL est correct

Par défaut, MySQL utilise le port **3306**. Si votre MySQL utilise un autre port, modifiez l'URL dans `hibernate.cfg.xml` :

```xml
<property name="connection.url">jdbc:mysql://localhost:VOTRE_PORT/phone_shoop</property>
```

### 6. Création automatique des tables

Grâce à la configuration `hbm2ddl.auto=update` dans `hibernate.cfg.xml`, Hibernate créera automatiquement les tables lors du premier démarrage de l'application.

Les tables suivantes seront créées :
- **Category** :** Catégories de produits
- **Product** :** Produits
- **User** :** Utilisateurs

### 7. Tester la connexion

Après avoir configuré MySQL, redémarrez l'application :

```bash
mvn jetty:run
```

L'application devrait maintenant se connecter à MySQL sans erreur.

## Dépannage

### Erreur : "Access denied for user 'root'@'localhost'"
- Vérifiez que le nom d'utilisateur et le mot de passe dans `hibernate.cfg.xml` sont corrects
- Réinitialisez le mot de passe MySQL si nécessaire

### Erreur : "Unknown database 'phone_shoop'"
- Créez la base de données avec la commande SQL ci-dessus

### Erreur : "Communications link failure"
- Vérifiez que le service MySQL est démarré
- Vérifiez que le port 3306 n'est pas bloqué par un firewall
- Vérifiez que l'URL de connexion dans `hibernate.cfg.xml` est correcte

### Erreur : "ClassNotFoundException: com.mysql.jdbc.Driver"
- Le driver MySQL est déjà dans le `pom.xml`, mais si l'erreur persiste, exécutez :
  ```bash
  mvn clean install
  ```

