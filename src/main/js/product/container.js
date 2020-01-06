import { Product } from './product'

const React = require('react');
var $ = require('jquery');

class Container extends React.Component {
  mapProducts() {
	  console.log("From mapProducts");
	  let productsFilterred = [];
	  let snapshotProducts = this.props.snapshotProducts;
	  if (snapshotProducts != null) {
		  console.log("snapshotProducts is not null");
		  console.log("snapshotProducts => " + snapshotProducts);
		  $.each(snapshotProducts, (brandId, genders) => {
			  console.log("brandId = " + brandId);
			  $.each(genders, (genderId, categories) => {
				  console.log("genderId = " + genderId);
				  $.each(categories, (categoryId, products) => {
					  //console.log("categoryId = " + categoryId);
            let productComponents = $.grep(products, (product, idx) => {
              if (product.priceDiscount >= this.props.priceFilter.minPrice
                && product.priceDiscount <= this.props.priceFilter.maxPrice
                && (product.discount * 100) >= this.props.priceFilter.minDiscount
                && (product.discount * 100) <= this.props.priceFilter.maxDiscount) {
                  return true;
              } else {
                return false;
              }
            });

            productComponents = $.grep(productComponents, (product, idx) => {
              let categoryFilter = this.props.categoryFilter;
              if (categoryFilter.hasOwnProperty(product.brandId)
                && categoryFilter[product.brandId].hasOwnProperty(product.genderId)
                && ($.inArray(product.categoryId, categoryFilter[product.brandId][product.genderId]) != -1)) {
                  return true;
              } else {
                return false;
              }
            });

					  productsFilterred = productsFilterred.concat(productComponents);
				  });
			  });
		  });
	  }

    productsFilterred.sort((a, b) => {
      if (a.isNew > b.isNew) {
        return -1;
      } else if (a.isNew < b.isNew) {
        return 1;
      }

      if (a.priceDiscount < b.priceDiscount) {
        return -1;
      } else if (a.priceDiscount > b.priceDiscount) {
        return 1;
      }

      if (a.discount > b.discount) {
        return -1;
      } else if (a.discount < b.discount) {
        return 1;
      }

      if (a.brandId < b.brandId) {
        return -1;
      } else if (a.brandId > b.brandId) {
        return 1;
      }

      if (a.genderId < b.genderId) {
        return -1;
      } else if (a.genderId > b.genderId) {
        return 1;
      }

      if (a.categoryId < b.categoryId) {
        return -1;
      } else if (a.categoryId > b.categoryId) {
        return 1;
      }

      if (a.productId < b.productId) {
        return -1;
      } else if (a.productId > b.productId) {
        return 1;
      }

      return 0;
    });

    let productComponentsFilterred = $.map(productsFilterred, (product, idx) => {
      return <Product key={product.brandId + "-" + product.productId + "-" + product.isNew + "-" + product.priceDiscount} productInfo={product} onProductSearchDropdownItemClick={(productId) => this.props.onProductSearchDropdownItemClick(productId)} />;
    });

	  console.log("Done compiling product list; Total number of product: " + productComponentsFilterred.length);
	  return productComponentsFilterred;
  }

  render() {
    let snapshotProducts = this.mapProducts();
    return (
      <div className="monitor-container d-flex flex-wrap">
        {snapshotProducts}
      </div>
    );
  }
}

export { Container };
