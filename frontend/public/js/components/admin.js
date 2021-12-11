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
		if ($(e).prop('disabled')) return;
		$(e).prop('disabled', true);
		console.log("Will archive project " + id);
		$.post('/projects/' + id + '/archive').done((e) => {
			window.location.reload();
		}).fail((e) => {
			console.error("failed to archive project", e);
		});
	}
	deleteProject(e, id) {
		const $button = $(e.currentTarget);
		$button.prop('disabled', true);
		console.log("Will delete project " + id);
		$.post('/projects/' + id + "/delete").done((resp) => {
			const $element = $(`[data-prid="${id}"]`);
			$element.addClass('animate__animated animate__fadeOutUp');
			setTimeout(function () {
				$element.remove();
			}, 600);
		}).fail((err) => {
			console.error("Failed to delete project", err);
		});

	}
	getTerminals(tasks) {
		let terminalTasks = [];
		for (const task of tasks) {
			if (task.subtasks.length) {
				terminalTasks = [...terminalTasks, ...this.getTerminals(task.subtasks)];
			} else {
				terminalTasks.push(task);
			}
		}
		return terminalTasks;
	}

	renderProject(project) {
		const $proj = this.$adminCardTemplate = $('#adminCardTemplate').clone();
		$proj.removeClass('Template').removeAttr('id').attr('data-prid', project.id);
		$(this.adminProjTitle, $proj)
			.text(project.archived ? "[Archived] " + project.title : project.title)
			.removeAttr('id')
			.attr('href', '/pr/' + project.id);
		const terminalTasks = this.getTerminals(project.tasks);
		const totalTasks = terminalTasks.length;
		const incompleteTask = terminalTasks.reduce(function (prev, next) { return prev + !next.completed }, 0);
		const taskCompleted = terminalTasks.reduce(function (prev, next) { return prev + !!next.completed }, 0);
		const userCount = project.teammates.length;
		const percentComplete = (incompleteTask != 0) ? (taskCompleted / totalTasks * 100).toFixed(1) : 100;


		$(this.adminIncomplete, $proj).text(`${incompleteTask} Incomplete Tasks`).removeAttr('id');
		$(this.adminComplete, $proj).text(`${taskCompleted} Complete Tasks`).removeAttr('id');
		$(this.adminUserCount, $proj).text(`${userCount} Users`).removeAttr('id');
		$(this.adminPercentComplete, $proj).text(`${percentComplete}% complete`).removeAttr('id');

		if (project.archived)
			$(this.adminArchiveBtn, $proj).prop('disabled', true);
		else
			$(this.adminArchiveBtn, $proj).click((e) => this.archiveProject(e, project.id)).removeAttr('id');

		$(this.adminDeleteBtn, $proj).click((e) => this.deleteProject(e, project.id)).removeAttr('id');

		$proj.appendTo(this.$projectDeck);
	}
	cancelLoad() {
		this.$loader.remove();
	}
	fetchProjects() {
		$.get('/projects').done((projects) => {
			console.log(projects);
			this.cancelLoad();
			for (const project of projects.projects) {
				this.renderProject(project);
			}
		}).fail((err) => {
			console.error("Failed to retrieve projects", err);
		});
	}
}

export {
	Admin
};
