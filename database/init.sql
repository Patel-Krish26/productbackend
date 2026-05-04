-- USERS TABLE
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100),
    email NVARCHAR(100) UNIQUE,
    password NVARCHAR(255),
    role NVARCHAR(50) DEFAULT 'USER'
);

-- PRODUCTS TABLE
CREATE TABLE Products (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(150),
    description NVARCHAR(MAX),
    price DECIMAL(10,2),
    category NVARCHAR(100),
    stock INT,
    created_at DATETIME DEFAULT GETDATE()
);

-- PRODUCT IMAGES TABLE (🔥 IMPORTANT FOR MULTIPLE IMAGES)
CREATE TABLE ProductImages (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT,
    image_url NVARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE
);

-- CART TABLE
CREATE TABLE Cart (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

GO