-- Create Users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255), -- Nullable cho OAuth2 users
                       full_name VARCHAR(255),
                       provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL', -- LOCAL, GOOGLE
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create Categories table
CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            user_id UUID NOT NULL REFERENCES users(id),
                            name VARCHAR(100) NOT NULL,
                            type VARCHAR(20) NOT NULL, -- INCOME, EXPENSE
                            icon VARCHAR(50),
                            color VARCHAR(20),
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            CONSTRAINT uk_user_category_name UNIQUE (user_id, name)
);

-- Create Transactions table
CREATE TABLE transactions (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL REFERENCES users(id),
                              category_id UUID NOT NULL REFERENCES categories(id),
                              amount DECIMAL(19, 2) NOT NULL,
                              description TEXT,
                              transaction_date DATE NOT NULL,
                              created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                              updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Create Budgets table
CREATE TABLE budgets (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL REFERENCES users(id),
                         category_id UUID NOT NULL REFERENCES categories(id),
                         amount DECIMAL(19, 2) NOT NULL,
                         month INTEGER NOT NULL,
                         year INTEGER NOT NULL,
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         CONSTRAINT uk_user_category_month_year UNIQUE (user_id, category_id, month, year)
);