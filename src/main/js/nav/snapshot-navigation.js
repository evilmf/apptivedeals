const React = require('react');

class SnapshotNavigation extends React.Component {
  handleSnapshotChange(event) {
    console.log("From SnapshotNavigation.handleSnapshotChange");
    let userInputSnapshotId = event.target.value;
    if (Number(userInputSnapshotId)) {
      console.log("From SnapshotNavigation.handleSnapshotChange; " + userInputSnapshotId + " is number");
      this.props.onSnapshotChange(userInputSnapshotId);
    }
  }

  render() {
    return (
      <form>
				<div className="form-row">
					<div className="col-1">
						<a className="nav-link font-weight-bold text-success bg-white"
							href="#"
              onClick={() => this.props.onPreviousClick()}
              ><i className="fas fa-chevron-left"></i></a>
					</div>
					<div className="col">
						<input type="text" className="form-control"
            value={this.props.currentSnapshotId}
            onChange={(event) => this.handleSnapshotChange(event)}
            onClick={(event) => { event.target.select(); }}
            />
					</div>
					<span className="pt-1">/</span>
					<div className="col">
						<input type="text" className="form-control" value={this.props.latestSnapshotId} readOnly />
					</div>
					<div className="col-1">
						<a
							className="nav-link font-weight-bold text-success bg-white float-right pl-0"
							href="#"
              onClick={() => this.props.onNextClick()}
              ><i className="fas fa-chevron-right"></i></a>
					</div>
				</div>
			</form>
    );
  }
}

export { SnapshotNavigation };
