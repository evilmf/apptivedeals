const React = require('react');
var $ = require('jquery');

class ProductSnapshotHistoryRow extends React.Component {
	render() {
		return (
			<tr onClick={(event) => this.props.onSnapshotChange(this.props.productSnapshotInfo.snapshotId)}>
				<th scope="row">{this.props.productSnapshotInfo.snapshotId}</th>
				<td>{this.props.productSnapshotInfo.timeRange}</td>
				<td>{this.props.productSnapshotInfo.priceDiscount}</td>
			</tr>
		);
	}
}

class ProductSnapshotHistoryModal extends React.Component {
	mapProductSnapshotHistory() {
		console.log("From mapProductSnapshotHistory");
		let productSnapshotHistory = [];
		if (this.props.productSnapshotHistory != null) {
			productSnapshotHistory = $.map(this.props.productSnapshotHistory, (element, idx) => {
				return <ProductSnapshotHistoryRow key={element.productId} productSnapshotInfo={element} onSnapshotChange={(snapshotId) => this.props.onSnapshotChange(snapshotId)} />
			});
		}

		return productSnapshotHistory;
	}

	render() {
		let productSnapshotHistory = this.mapProductSnapshotHistory();
		return (
				<div className="modal fade" id="productSnapshotHistoryModal" tabIndex="-1" role="dialog"
					aria-labelledby="productSnapshotHistoryModalTitle" aria-hidden="true">
					<div className="modal-dialog modal-dialog-centered modal-xl" role="document">
						<div className="modal-content">
							<div className="modal-header">
				              Product Name
				              <button type="button" className="close" data-dismiss="modal" aria-label="Close">
				                <span aria-hidden="true">&times;</span>
				              </button>
				            </div>
				            <div className="modal-body">
					            <table className="table table-hover">
						            <thead>
						              <tr>
						                <th scope="col">Snapshot</th>
						                <th scope="col">Time Frame</th>
						                <th scope="col">Price</th>
						              </tr>
						            </thead>
						            <tbody>
						              {productSnapshotHistory}
						            </tbody>
					          </table>
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

export { ProductSnapshotHistoryModal };
