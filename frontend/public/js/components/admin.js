class Admin {
	constructor() {
		this.$projectDeck = $('#adminDeck');
		this.$adminCardTemplate = $('#adminCardTemplate');
		this.$loader = $('#adminLoader');

		this.adminProjTitle = "#adminProjTitle";
		this.adminIncomplete = "#adminIncomplete";
		this.adminComplete = "#adminComplete";
		this.adminUserCount = "#adminUserCount";
		this.adminPercentComplete = "#adminPercentComplete";
		this.adminArchiveBtn = "#adminArchiveBtn";
		this.adminDeleteBtn = "#adminDeleteBtn";

		this.fetchProjects();
	}
	archiveProject(e, id) {
		console.log("Will archive project "+id);
	}
	deleteProject(e, id) {
		const $button = $(e.currentTarget);
		$button.prop('disabled', true);
		console.log("Will delete project "+id);
		$.post('/projects/'+id+"/delete").done((resp) => {
			const $element = $(`[data-prid="${id}"]`);
			$element.addClass('animate__animated animate__fadeOutUp');
			setTimeout(function() {
				$element.remove();
			}, 600);
		}).fail((err) => {
			console.error("Failed to delete project", err);
		});

	}
	renderProject(project) {
		const $proj = this.$adminCardTemplate = $('#adminCardTemplate').clone();
		$proj.removeClass('Template').removeAttr('id').attr('data-prid', project.projectId);
		$(this.adminProjTitle, $proj)
			.text(project.archived ? "[Archived] "+project.title : project.title)
			.removeAttr('id')
			.attr('href', '/pr/'+project.projectId);
		$(this.adminIncomplete, $proj).text("0 Incomplete Tasks").removeAttr('id');
		$(this.adminComplete, $proj).text("0 Complete Tasks").removeAttr('id');
		$(this.adminUserCount, $proj).text("0 Users").removeAttr('id');
		$(this.adminPercentComplete, $proj).text("100% complete").removeAttr('id');

		if (project.archived)
			$(this.adminArchiveBtn, $proj).prop('disabled', true);
		else
			$(this.adminArchiveBtn, $proj).click( (e) => this.archiveProject(e, project.projectId)).removeAttr('id');

		$(this.adminDeleteBtn, $proj).click( (e) => this.deleteProject(e, project.projectId)).removeAttr('id');

		$proj.appendTo(this.$projectDeck);
	}
	cancelLoad() {
		this.$loader.remove();
	}
	fetchProjects() {
		$.get('/projects').done( (projects) => {
			console.log(projects);
			this.cancelLoad();
			for (const project of projects.projects) {
				this.renderProject(project);
			}
		}).fail( (err) => {
			console.error("Failed to retrieve projects", err);
		});
	}
}

export {
	Admin
};
