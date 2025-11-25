-- Script pour insérer des données de test dans la base de données phone_shoop
-- Exécutez ce script après avoir créé la base de données

USE phone_shoop;

-- Insérer des catégories
INSERT INTO Category (categoryTitle, categoryDescription) VALUES
('Smartphones', 'Latest smartphones with advanced features and cutting-edge technology'),
('Tablets', 'Portable tablets for work and entertainment'),
('Accessories', 'Phone cases, chargers, headphones and other accessories'),
('Wearables', 'Smartwatches and fitness trackers');

-- Insérer des produits (assurez-vous que les categoryId correspondent aux catégories insérées)
-- Note: Les IDs peuvent varier, ajustez-les selon vos données

-- Produits pour Smartphones (categoryId = 1)
INSERT INTO Product (pName, pDesc, pPrice, pDiscount, pQuantity, pPhoto, category_categoryId) VALUES
('iPhone 15 Pro', 'Latest iPhone with A17 Pro chip, titanium design, and advanced camera system. Features 6.1-inch Super Retina XDR display, 48MP main camera, and all-day battery life.', 1199, 10, 50, 'iphone15pro.png', 1),
('Samsung Galaxy S24 Ultra', 'Premium Android flagship with S Pen, 200MP camera, and Snapdragon 8 Gen 3. Includes 6.8-inch Dynamic AMOLED display and 5000mAh battery.', 1299, 15, 40, 'galaxy-s24-ultra.png', 1),
('Google Pixel 8 Pro', 'Pure Android experience with Google AI features. 6.7-inch LTPO OLED display, triple camera system with Super Res Zoom, and 7 years of updates.', 999, 5, 60, 'pixel8pro.png', 1),
('OnePlus 12', 'Flagship killer with Snapdragon 8 Gen 3, 100W fast charging, and Hasselblad camera. Features 6.82-inch LTPO display and premium design.', 799, 12, 45, 'oneplus12.png', 1);

-- Produits pour Tablets (categoryId = 2)
INSERT INTO Product (pName, pDesc, pPrice, pDiscount, pQuantity, pPhoto, category_categoryId) VALUES
('iPad Pro 12.9"', 'Professional tablet with M2 chip, 12.9-inch Liquid Retina XDR display, and Apple Pencil support. Perfect for creative professionals.', 1099, 8, 30, 'ipad-pro.png', 2),
('Samsung Galaxy Tab S9', 'Premium Android tablet with S Pen, 12.4-inch AMOLED display, and Snapdragon 8 Gen 2. Water-resistant design.', 899, 10, 35, 'galaxy-tab-s9.png', 2),
('Microsoft Surface Pro 9', '2-in-1 laptop-tablet hybrid with Intel Core i7, 13-inch PixelSense display, and detachable keyboard. Perfect for productivity.', 1299, 5, 25, 'surface-pro9.png', 2);

-- Produits pour Accessories (categoryId = 3)
INSERT INTO Product (pName, pDesc, pPrice, pDiscount, pQuantity, pPhoto, category_categoryId) VALUES
('Wireless Charging Pad', 'Fast wireless charging pad compatible with Qi-enabled devices. Supports up to 15W charging speed with LED indicator.', 29, 20, 100, 'wireless-charger.png', 3),
('Bluetooth Earbuds Pro', 'Premium wireless earbuds with active noise cancellation, 30-hour battery life, and IPX7 water resistance. Crystal clear sound quality.', 149, 15, 80, 'bluetooth-earbuds.png', 3),
('Phone Case - Clear', 'Durable clear protective case with shock absorption. Maintains phone design visibility while providing full protection.', 19, 25, 150, 'phone-case-clear.png', 3),
('USB-C Fast Charger', '65W fast charging adapter with USB-C port. Compatible with most modern smartphones and tablets. Includes 6ft cable.', 39, 10, 120, 'fast-charger.png', 3);

-- Produits pour Wearables (categoryId = 4)
INSERT INTO Product (pName, pDesc, pPrice, pDiscount, pQuantity, pPhoto, category_categoryId) VALUES
('Apple Watch Series 9', 'Latest smartwatch with S9 SiP, always-on Retina display, and advanced health features. GPS and cellular options available.', 399, 5, 40, 'apple-watch-9.png', 4),
('Samsung Galaxy Watch 6', 'Premium smartwatch with rotating bezel, advanced health tracking, and 40-hour battery life. Compatible with Android and iOS.', 299, 12, 50, 'galaxy-watch-6.png', 4),
('Fitbit Charge 6', 'Fitness tracker with heart rate monitoring, sleep tracking, and 7-day battery life. Water-resistant and lightweight design.', 159, 15, 70, 'fitbit-charge6.png', 4);

-- Vérifier les données insérées
SELECT 'Categories insérées:' AS Info;
SELECT * FROM Category;

SELECT 'Produits insérés:' AS Info;
SELECT pId, pName, pPrice, pDiscount, pQuantity, category_categoryId FROM Product;

SELECT 'Total de produits par catégorie:' AS Info;
SELECT c.categoryTitle, COUNT(p.pId) as nombre_produits 
FROM Category c 
LEFT JOIN Product p ON c.categoryId = p.category_categoryId 
GROUP BY c.categoryId, c.categoryTitle;

