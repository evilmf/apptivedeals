const React = require('react');
var $ = require('jquery');

class ProductSearchDropdownItem extends React.Component {
  render() {
    return (
      <li className="list-group-item p-0">
        <a href="#" className="text-decoration-none" data-toggle="modal" data-target="#productSnapshotHistoryModal" onClick={(event) => this.props.onProductSearchDropdownItemClick(this.props.productInfo.productId)}>
          <div className="card border-0">
            <div className="row no-gutters">
              <div className="col-8">
                <div className="card-body p-1 pb-0">
                  <ul className="list-group">
                    <li className="list-group-item p-0 border-0 card-title"><small>{this.props.productInfo.productName}</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small>{this.props.productInfo.brand}</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small>{this.props.productInfo.category}</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small className="text-muted float-left">{this.props.productInfo.gender}</small><small className="text-muted float-right">${this.props.productInfo.minPrice} - ${this.props.productInfo.maxPrice}</small></li>
                  </ul>
                </div>
              </div>
              <div className="col-4">
                <img src={this.props.productInfo.imageUrl} className="card-img rounded-0 float-right" style={{height: '6rem', width: '4rem'}} alt={this.props.productInfo.productName} />
              </div>
            </div>
          </div>
        </a>
      </li>
    );
  }
}

class ProductSearchDropdown extends React.Component {
  mapSearchResult() {
	console.log("From mapSearchResult");
	let products = [];
	let productSearchResult = this.props.productSearch.productSearchResult;
	if (productSearchResult != null) {
		products = $.map(productSearchResult, (element, idx) => {
			return <ProductSearchDropdownItem key={element.productId} productInfo={element} onProductSearchDropdownItemClick={(productId) => this.props.onProductSearchDropdownItemClick(productId)} />;
		});
	}

	return products;
  }

  render() {
	let productSearchResult = this.mapSearchResult();
    return (
      <ul className="list-group position-absolute pl-3 w-100" style={{overflowY: 'scroll', maxHeight: '800%'}}>
        {productSearchResult}
      </ul>
    );
  }
}

export { ProductSearchDropdown };
