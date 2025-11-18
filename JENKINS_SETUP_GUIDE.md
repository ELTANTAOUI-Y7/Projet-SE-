# Guide de Configuration et Exécution Jenkins avec SonarQube

## ✅ État Actuel
- ✅ Jenkins: **Démarré** (port 8080)
- ✅ SonarQube: **Démarré** (port 9000)
- ✅ Jenkinsfile: **Configuré** avec méthode A (withSonarQubeEnv)
- ✅ pom.xml: **Plugin SonarQube ajouté**

---

## 📋 Checklist de Configuration

### 1. Configuration des Outils Globaux dans Jenkins

#### A. JDK Configuration
1. Allez dans `Manage Jenkins` → `Global Tool Configuration`
2. Section **JDK** → Cliquez sur `Add JDK`
3. Configurez:
   - **Name**: `JDK11` (doit correspondre au Jenkinsfile)
   - **JAVA_HOME**: Chemin vers votre JDK 11 (ex: `C:\Program Files\Java\jdk-11`)
   - OU cochez "Install automatically" et sélectionnez la version

#### B. Maven Configuration
1. Dans la même page, section **Maven**
2. Cliquez sur `Add Maven`
3. Configurez:
   - **Name**: `Maven3` (doit correspondre au Jenkinsfile)
   - **MAVEN_HOME**: Chemin vers Maven (ex: `C:\Program Files\Apache\maven`)
   - OU cochez "Install automatically" et sélectionnez la version 3.x

---

### 2. Configuration SonarQube dans Jenkins

#### A. Créer le Token SonarQube
1. Ouvrez SonarQube: `http://localhost:9000`
2. Connectez-vous (admin/admin par défaut)
3. Cliquez sur votre avatar → `My Account` → `Security`
4. Section `Generate Tokens`:
   - **Token name**: `Jenkins Integration`
   - Cliquez `Generate`
   - **COPIEZ LE TOKEN** (vous ne le reverrez plus!)

#### B. Ajouter le Token dans Jenkins Credentials
1. Dans Jenkins: `Manage Jenkins` → `Credentials` → `System` → `Global credentials (unrestricted)`
2. Cliquez `Add Credentials`
3. Configurez:
   - **Kind**: `Secret text`
   - **Secret**: Collez le token SonarQube
   - **ID**: `sonar-token`
   - **Description**: `SonarQube analysis token`
4. Cliquez `OK`

#### C. Configurer le Serveur SonarQube
1. Dans Jenkins: `Manage Jenkins` → `Configure System`
2. Section `SonarQube servers` → Cliquez `Add SonarQube`
3. Configurez:
   - **Name**: `Sonar-Server` (doit correspondre au Jenkinsfile ligne 178)
   - **Server URL**: `http://localhost:9000`
   - ✅ Cochez `Use Secret Texts or Files`
   - **Server authentication token**: Sélectionnez `sonar-token`
4. Cliquez `Save`

---

### 3. Créer le Projet dans SonarQube

1. Dans SonarQube: `http://localhost:9000`
2. Allez dans `Projects` → `Create Project`
3. Configurez:
   - **Project key**: `phone_shoop` (doit correspondre au Jenkinsfile)
   - **Display name**: `Simple E-commerce Website`
4. Cliquez `Set Up`
5. Sélectionnez `With Maven` (nous utilisons le plugin Maven)
6. Notez la clé du projet (sera utilisée automatiquement)

---

### 4. Créer le Job Pipeline dans Jenkins

#### Option A: Pipeline Job (Recommandé pour début)
1. Dans Jenkins: `New Item`
2. Entrez un nom: `Simple-Ecommerce-Pipeline`
3. Sélectionnez `Pipeline`
4. Cliquez `OK`

#### Configuration du Pipeline:
1. **Description**: `Pipeline CI/CD avec SonarQube pour Simple E-commerce Website`

2. **Pipeline Section**:
   - **Definition**: `Pipeline script from SCM`
   - **SCM**: `Git`
   - **Repository URL**: URL de votre repository Git
   - **Credentials**: Sélectionnez `git-creds` (si configuré)
   - **Branch**: `*/main` ou `*/master`
   - **Script Path**: `Jenkinsfile` (par défaut)

3. Cliquez `Save`

#### Option B: Multibranch Pipeline (Avancé)
1. `New Item` → `Multibranch Pipeline`
2. Configurez les `Branch Sources` (GitHub, GitLab, etc.)
3. Le Jenkinsfile sera automatiquement détecté sur chaque branche

---

### 5. Exécuter le Build

1. Dans votre job Jenkins, cliquez sur `Build Now`
2. Surveillez la console en temps réel:
   - Cliquez sur le build en cours
   - Cliquez sur `Console Output`

#### Ce qui va se passer:
1. ✅ **Checkout**: Récupération du code
2. ✅ **Build**: Compilation du projet
3. ✅ **Test**: Exécution des tests unitaires
4. ✅ **Package**: Création du fichier WAR
5. ✅ **SonarQube Analysis**: Analyse du code
6. ✅ **Quality Gate**: Vérification des critères de qualité

---

## 🔍 Vérification des Résultats

### Dans Jenkins:
- **Console Output**: Logs détaillés de chaque stage
- **Test Results**: Résultats des tests unitaires
- **SonarQube Quality Gate**: Statut du Quality Gate
- **Artifacts**: Fichier WAR généré

### Dans SonarQube:
1. Allez sur `http://localhost:9000`
2. Projet `phone_shoop`
3. Vérifiez:
   - **Bugs**: Nombre de bugs détectés
   - **Code Smells**: Problèmes de qualité
   - **Coverage**: Couverture de code
   - **Security Hotspots**: Problèmes de sécurité

---

## 🐛 Résolution de Problèmes

### Erreur: "Tool 'JDK11' not found"
- Vérifiez que JDK11 est configuré dans `Global Tool Configuration`
- Le nom doit correspondre exactement: `JDK11`

### Erreur: "Tool 'Maven3' not found"
- Vérifiez que Maven3 est configuré dans `Global Tool Configuration`
- Le nom doit correspondre exactement: `Maven3`

### Erreur: "Sonar-Server not found"
- Vérifiez la configuration dans `Configure System` → `SonarQube servers`
- Le nom doit correspondre exactement: `Sonar-Server`

### Erreur: "Quality Gate failed"
- Vérifiez les résultats dans SonarQube
- Ajustez les règles du Quality Gate si nécessaire
- Le pipeline s'arrêtera si le Quality Gate échoue (comportement normal)

### Erreur: "Cannot connect to SonarQube"
- Vérifiez que SonarQube est démarré: `http://localhost:9000`
- Vérifiez l'URL dans la configuration Jenkins
- Vérifiez que le token est valide

---

## 📊 Métriques Attendues

Après l'exécution réussie, vous devriez voir:
- ✅ Build réussi avec tous les stages verts
- ✅ Tests unitaires exécutés et rapportés
- ✅ Fichier WAR archivé
- ✅ Analyse SonarQube complétée
- ✅ Quality Gate passé
- ✅ Rapport détaillé dans SonarQube

---

## 🚀 Prochaines Étapes

1. Automatiser les builds sur push Git (webhook)
2. Ajouter des notifications (email, Slack)
3. Configurer le déploiement automatique
4. Améliorer la couverture de code
5. Configurer des Quality Gates plus stricts

---

**Besoin d'aide?** Consultez les logs dans Jenkins Console Output pour plus de détails.
