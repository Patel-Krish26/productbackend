-- =========================
-- USERS TABLE
-- =========================
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    role NVARCHAR(50) DEFAULT 'USER'
);

-- =========================
-- PRODUCTS TABLE
-- =========================
CREATE TABLE Products (
    id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(150) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10,2) NOT NULL,
    category NVARCHAR(100),
    stock INT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE()
);

-- =========================
-- PRODUCT IMAGES
-- =========================
CREATE TABLE ProductImages (
    id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT NOT NULL,
    image_url NVARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE
);

-- =========================
-- CART
-- =========================
CREATE TABLE Cart (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE
);

-- =========================
-- ORDERS
-- =========================
CREATE TABLE Orders (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(50) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- =========================
-- ORDER ITEMS
-- =========================
CREATE TABLE OrderItems (
    id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    image_url NVARCHAR(255), -- snapshot of image
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);
GO