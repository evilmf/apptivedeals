const React = require('react');

class SnapshotTimestamp extends React.Component {
  render() {
    return (
      <span className="text-secondary pl-3">{(new Date(this.props.snapshotTimestamp)).toLocaleString()}</span>
    );
  }
}

export { SnapshotTimestamp };
