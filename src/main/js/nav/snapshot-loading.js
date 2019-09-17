const React = require('react');

class SnapshotLoading extends React.Component {
  render() {
    return (
      <div className={"spinner-border spinner-border-sm ml-2 text-primary " + (this.props.isLoading ? "visible" : "invisible")}
        role="status">
				<span className="sr-only">Loading...</span>
			</div>
    );
  }
}

export { SnapshotLoading };
