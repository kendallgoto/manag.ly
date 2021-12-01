class Home {
	constructor() {
		this.$newProjectName = $('#landing #createName');
		this.$createProjBtn = $('#landing #createProjectBtn');
		this.$recoverProjId = $('#landing #recoverProject');
		this.$openProjBtn = $('#landing #openProjectBtn');
		this.$createProjErr = $('#landing #createNameError');
		this.setupHandlers();
	}
	setupHandlers() {
		this.$openProjBtn.click(() => {
			const projectId = this.$recoverProjId.val();
			window.location.href = "/pr/"+projectId;
		});
		this.$createProjBtn.click(() => {
			this.$createProjBtn.prop('disabled', true);
			const newProjectName = this.$newProjectName.val();
			$.post('/projects', JSON.stringify({ title: newProjectName})).done((resp) => {
				console.log("Created new project", resp);
				if (resp.projectId) {
					window.location.href = "/pr/"+resp.projectId;
				}
			}).fail((err) => {
				console.log("Failed to create new project", err);
				this.$newProjectName.addClass('is-invalid');
				this.$createProjErr.text(err.responseJSON?.message ?? "An unknown error occurred. Please try again.");
				this.$createProjBtn.prop('disabled', false);
			});
		});
	}
}

export {
	Home
};
