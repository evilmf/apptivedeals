const React = require('react');
var $ = require('jquery');

class CategoryFilterOption extends React.Component {
  render() {
    return (
      <option value={this.props.value}>{this.props.display}</option>
    );
  }
}

class CategoryFilterCheckBox extends React.Component {
  render() {
    return (
      <div className="form-group form-check col-12 col-sm-6">
        <input className="form-check-input" type="checkbox" id={this.props.brandId + "-" + this.props.genderId + "-" + this.props.categoryId}
          checked={this.props.checked}
          onChange={(event) => this.props.onClick(event.target.checked)} />
        <label className="form-check-label text-nowrap" htmlFor={this.props.brandId + "-" + this.props.genderId + "-" + this.props.categoryId}>
          {this.props.category}
        </label>
      </div>
    );
  }
}

class CategoryFilterModal extends React.Component {
  constructor(props) {
    super(props);

    this.state = {selectedBrand: null, selectedGender: null};
  }

  buildOptions() {
    let products = this.props.snapshotProducts;
    let optionComponents = [];
    if (products != null) {
      $.each(products, (brandId, genders) => {
        $.each(genders, (genderId, categories) => {
          let categoryIds = Object.getOwnPropertyNames(categories);
          let product = categories[categoryIds[0]][0];

          let brandName = product['brand'];
          let genderName = product['gender'];

          optionComponents.push(<CategoryFilterOption value={brandId + '-' + genderId} key={brandId + '-' + genderId} display={brandName + " - " + genderName} />);
        });
      });
    }

    return optionComponents;
  }

  buildCheckBoxes() {
    let brandId = this.state.selectedBrand;
    let genderId = this.state.selectedGender;
    let products = this.props.snapshotProducts;
    let checkBoxComponents = [];

    if (brandId != null && genderId != null
        && products != null
        && products.hasOwnProperty(brandId)
        && products[brandId].hasOwnProperty(genderId)) {
          let categories = products[brandId][genderId];
          let categoryFilter = null;
          if (this.props.categoryFilter != null
            && this.props.categoryFilter.hasOwnProperty(brandId)
            && this.props.categoryFilter[brandId].hasOwnProperty(genderId)) {
              categoryFilter = new Set(this.props.categoryFilter[brandId][genderId]);
            }
          else {
            categoryFilter = new Set();
          }
          $.each(categories, (categoryId, categoryProducts) => {
            if ($.isArray(categoryProducts)
                && categoryProducts.length > 0) {
                  let product = categoryProducts[0];
                  let productCategoryName = product.category;
                  let productCategoryId = product.categoryId;
                  let productBrandId = product.brandId;
                  let productGenderId = product.genderId;

                  checkBoxComponents.push(<CategoryFilterCheckBox
                    key={productBrandId + "-" + productGenderId + "-" + productCategoryId}
                    brandId={productBrandId}
                    genderId={productGenderId}
                    categoryId={productCategoryId}
                    category={productCategoryName}
                    checked={categoryFilter.has(productCategoryId)}
                    onClick={(checked) => this.props.onCategoryFilterChange(productBrandId, productGenderId, productCategoryId, checked)}
                     />);
            }
          });

          checkBoxComponents.sort(function(a, b) {
            if (a.props.category > b.props.category) {
              return 1;
            } else if (a.props.category < b.props.category) {
              return -1;
            }

            return 0;
          });
    }

    return checkBoxComponents;
  }

  componentDidMount() {
    console.log("From CategoryFilterModal.componentDidMount");
    let products = this.props.snapshotProducts;

    if (products != null) {
      let brandIds = Object.getOwnPropertyNames(products);
      let selectedBrandId = null;
      let selectedGenderId = null;
      if (brandIds.length > 0) {
        selectedBrandId = brandIds[0];

        let genderIds = Object.getOwnPropertyNames(products[selectedBrandId]);
        if (genderIds.length > 0) {
          selectedGenderId = genderIds[0];
        }
      }

      this.setState({selectedBrand: selectedBrandId, selectedGender: selectedGenderId});
    }
  }

  handleCategorySelectFilterChange(event) {
    console.log("Category filter selected: " + event.target.value);
    let ids = event.target.value.split("-");
    this.setState({selectedBrand: ids[0], selectedGender: ids[1]});
  }

  render() {
    let options = this.buildOptions();
    let checkboxes = this.buildCheckBoxes();

    return (
      <div className="modal fade" id="categoryFilterModal" tabIndex="-1" role="dialog" aria-labelledby="categoryFilterModalTitle" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered" role="document">
          <div className="modal-content">
            <div className="modal-header">
              Category Filter
              <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <div className="input-group mb-3">
                <div className="input-group-prepend">
                  <button className="rounded-left" type="button">Brand/Gender</button>
                </div>
                <select className="custom-select" onChange={(event) => this.handleCategorySelectFilterChange(event)} value={this.state.selectedBrand + "-" + this.state.selectedGender} id="categoryFilterSelection" aria-label="choose category filter to display">
                  {options}
                </select>
              </div>
              <div>
                <form className="d-flex flex-wrap">
                  {checkboxes}
                </form>
              </div>
            </div>
            <div className="modal-footer">
              <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export { CategoryFilterModal };
