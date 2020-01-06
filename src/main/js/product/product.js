const React = require('react');

class Product extends React.Component {
  render() {
    return (
      <div className="card m-1 ad-card-dimension position-relative">
      	<a href={this.props.productInfo.productURL} target="_blank">
      				<span className={"badge badge-pill badge-primary position-absolute new-product-badge " + (this.props.productInfo.isNew ? "d-block" : "d-none")}>NEW</span>
      				<img
						src={this.props.productInfo.imageURL}
						className="card-img-top" alt={this.props.productInfo.productName} />
				</a>
				<div className="card-body p-2">
					<h5 className="card-title">{this.props.productInfo.brand}</h5>
					<h6 className="card-subtitle mb-2 text-muted text-nowrap">{this.props.productInfo.category}</h6>
					<a href="#" className="text-decoration-none" data-toggle="modal" data-target="#productSnapshotHistoryModal" onClick={(event) => this.props.onProductSearchDropdownItemClick(this.props.productInfo.productId)} >
            <p className="card-text m-0 ad-product-name">{this.props.productInfo.productName}</p>
          </a>
					<p className="card-text float-right text-secondary">${this.props.productInfo.priceDiscount}</p>
				</div>
				<div className="card-footer text-muted">
					<div className="d-flex">
						<div className="col p-0">{this.props.productInfo.gender}</div>
						<div className="col p-0">{Math.round(this.props.productInfo.discount * 100)}% Off</div>
					</div>
				</div>
		  </div>
    );
  }
}

export { Product };
