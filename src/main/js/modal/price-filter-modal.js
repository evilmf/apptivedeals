const React = require('react');

class PriceFilterModal extends React.Component {
  handleMinPriceChange(event) {
    let minPrice = event.target.value;
    if (minPrice >= 0) {
      this.props.onPriceFilterChange({minPrice: minPrice});
    }
  }

  handleMaxPriceChange(event) {
    let maxPrice = event.target.value;
    if (maxPrice >= 0) {
      this.props.onPriceFilterChange({maxPrice: maxPrice});
    }
  }

  handleMinDiscountChange(event) {
    let minDiscount = event.target.value;
    if (minDiscount >= 0 && minDiscount < 100) {
      this.props.onPriceFilterChange({minDiscount: minDiscount});
    }
  }

  handleMaxDiscountChange(event) {
    let maxDiscount = event.target.value;
    if (maxDiscount >= 0 && maxDiscount <= 100 ) {
      this.props.onPriceFilterChange({maxDiscount: maxDiscount});
    }
  }

  render() {
    return (
      <div className="modal fade" id="priceFilterModal" tabIndex="-1" role="dialog" aria-labelledby="priceFilterModalTitle" aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered" role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="priceFilterModalTitle">Price Filter</h5>
              <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <form>
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="minimumPrice">Min Price</label>
                    <input type="number" className="form-control" id="minimumPrice"
                      value={this.props.priceFilter.minPrice}
                      onChange={(event) => {this.handleMinPriceChange(event);}} />
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="maximumPrice">Max Price</label>
                    <input type="number" className="form-control" id="maximumPrice"
                    value={this.props.priceFilter.maxPrice}
                    onChange={(event) => {this.handleMaxPriceChange(event);}} />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="minimumDiscount">Min Discount (%)</label>
                    <input type="number" className="form-control" id="minimumDiscount"
                    value={this.props.priceFilter.minDiscount}
                    onChange={(event) => {this.handleMinDiscountChange(event);}} />
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="maximumDiscount">Max Discount (%)</label>
                    <input type="number" className="form-control" id="maximumDiscount"
                    value={this.props.priceFilter.maxDiscount}
                    onChange={(event) => {this.handleMaxDiscountChange(event);}} />
                  </div>
                </div>
              </form>
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

export { PriceFilterModal };
