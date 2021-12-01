class ProjectView {
	constructor() {
		this.$navProjTitle = $("#navProjTitle");
		this.$projectTitle = $("#projectTitle");
		this.$projectTeamList = $("#projectTeamList");

		this.fetchProject(window.projectId);
	}
	renderProject(project) {
		this.$navProjTitle.text(project.title);
		this.$projectTitle.text(project.title);
		$('#projectView > .container').addClass('animate__animated animate__fadeIn');
	}
	fetchProject() {
		$.get('/projects/'+window.projectId).done( (project) => {
			console.log(project);
			this.renderProject(project);
		}).fail( (err) => {
			console.error("Failed to retrieve project", err);
		});
	}
}

export {
	ProjectView
};
