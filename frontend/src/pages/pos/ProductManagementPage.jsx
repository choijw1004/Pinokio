import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import ProductList from '../../components/pos/ProductList';
import ProductModal from '../../components/pos/ProductModal';
import CategoryList from '../../components/pos/CategoryList';
import CategoryModal from '../../components/pos/CategoryModal';
import Button from '../../components/common/Button';
import Toast from '../../components/common/Toast';
import { getItems } from '../../apis/Item'; // Import the API function
import { useSelector } from 'react-redux'; // Assuming you use Redux to get posId

const TabContainer = styled.div`
  display: flex;
  margin-bottom: 20px;
`;

const Tab = styled.div`
  padding: 10px 20px;
  cursor: pointer;
  background-color: ${(props) => (props.active ? '#007bff' : '#f8f9fa')};
  color: ${(props) => (props.active ? 'white' : 'black')};
`;

const ProductManagementPage = () => {
  const [activeTab, setActiveTab] = useState('상품');
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [isProductModalOpen, setIsProductModalOpen] = useState(false);
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [toastMessage, setToastMessage] = useState('');
  const [selectedCategoryFilter, setSelectedCategoryFilter] = useState('전체');
  const posId = 'b48b8afe-0eec-4048-9b47-ebfffb190866';

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const data = await getItems(posId);
        setProducts(data.products || []); // Adjust based on actual API response
        setCategories(data.categories || []); // Adjust based on actual API response
        console.log(data.map((name) => `${name.name}`));
      } catch (error) {
        setToastMessage('상품 및 카테고리 데이터를 가져오는 데 실패했습니다.');
      }
    };

    fetchItems();
  }, [posId]);

  const handleAddProduct = () => {
    setSelectedProduct(null);
    setIsProductModalOpen(true);
  };

  const handleEditProduct = (product) => {
    setSelectedProduct(product);
    setIsProductModalOpen(true);
  };

  const handleToggleProduct = (product) => {
    setProducts(products.map((p) => (p.id === product.id ? product : p)));
    setToastMessage(`${product.name} 상품 정보가 업데이트되었습니다.`);
  };

  const handleDeleteProduct = (productId) => {
    setProducts(products.filter((p) => p.id !== productId));
    setToastMessage(`${products.find((p) => p.id === productId).name} 상품 삭제 완료!`);
  };

  const handleSaveProduct = (product) => {
    if (product.id) {
      setProducts(products.map((p) => (p.id === product.id ? product : p)));
      setToastMessage(`${product.name} 상품 수정 완료!`);
    } else {
      const newProduct = { ...product, id: Date.now() };
      setProducts([...products, newProduct]);
      setToastMessage(`${product.name} 상품 추가 완료!`);
    }
    setIsProductModalOpen(false);
  };

  const handleAddCategory = () => {
    setSelectedCategory(null);
    setIsCategoryModalOpen(true);
  };

  const handleEditCategory = (category) => {
    setSelectedCategory(category);
    setIsCategoryModalOpen(true);
  };

  const handleDeleteCategory = (categoryId) => {
    setCategories(categories.filter((c) => c.id !== categoryId));
    setToastMessage(`${categories.find((c) => c.id === categoryId).name} 카테고리 삭제 완료!`);
  };

  const handleSaveCategory = (category) => {
    if (category.id) {
      setCategories(categories.map((c) => (c.id === category.id ? category : c)));
      setToastMessage(`${category.name} 카테고리 수정 완료!`);
    } else {
      const newCategory = { ...category, id: Date.now() };
      setCategories([...categories, newCategory]);
      setToastMessage(`${category.name} 카테고리 추가 완료!`);
    }
    setIsCategoryModalOpen(false);
  };

  const filteredProducts =
    selectedCategoryFilter === '전체'
      ? products
      : products.filter((product) => product.category === selectedCategoryFilter);

  return (
    <div>
      <TabContainer>
        <Tab active={activeTab === '상품'} onClick={() => setActiveTab('상품')}>
          상품
        </Tab>
        <Tab active={activeTab === '카테고리'} onClick={() => setActiveTab('카테고리')}>
          카테고리
        </Tab>
      </TabContainer>

      {activeTab === '상품' && (
        <>
          <Button onClick={handleAddProduct} text="상품 추가" />
          <select
            value={selectedCategoryFilter}
            onChange={(e) => setSelectedCategoryFilter(e.target.value)}
          >
            <option value="전체">전체</option>
            {categories.map((category) => (
              <option key={category.id} value={category.name}>
                {category.name}
              </option>
            ))}
          </select>
          <ProductList
            products={filteredProducts}
            onEdit={handleEditProduct}
            onDelete={handleDeleteProduct}
            onToggle={handleToggleProduct}
          />
          {isProductModalOpen && (
            <ProductModal
              product={selectedProduct}
              categories={categories}
              onSave={handleSaveProduct}
              onClose={() => setIsProductModalOpen(false)}
            />
          )}
        </>
      )}

      {activeTab === '카테고리' && (
        <>
          <Button onClick={handleAddCategory} text="카테고리 추가" />
          <CategoryList
            categories={categories}
            onEdit={handleEditCategory}
            onDelete={handleDeleteCategory}
          />
          {isCategoryModalOpen && (
            <CategoryModal
              category={selectedCategory}
              onSave={handleSaveCategory}
              onClose={() => setIsCategoryModalOpen(false)}
            />
          )}
        </>
      )}

      {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}
    </div>
  );
};

export default ProductManagementPage;
