class TeamView {
	constructor() {
		this.$navProjTitle = $("#teamView #navProjTitle");
		this.$projectTitle = $("#teamView #projectTitle");
		this.$projectTeamList = $("#teamView #projectTeamList");
		this.$projectUserTemplate = $("#teamView #projectUserTemplate");

		this.$addUserName = $('#teamView #addUserName');
		this.$addUserError = $('#teamView #addUserError');
		this.$addUserBtn = $('#teamView #addUserBtn');
		this.fetchProject(window.projectId);
		this.registerHandlers();
	}
	truncateName(name) {
		console.log(name);
		const splitted = name.split(' ');
		let resultName = "";
		resultName += splitted[0].charAt(0).toUpperCase();
		if (splitted.length > 1)
			resultName += splitted[1].charAt(0).toUpperCase();
		return resultName;
	}
	renderProject(project) {
		this.$navProjTitle.text(project.title);
		this.$projectTitle.text(project.title);
		$('#teamView > .container').addClass('animate__animated animate__fadeIn');
		if (project.teammates) {
			for (const teammate of project.teammates) this.renderTeammate(teammate);
		}
	}
	renderTeammate(teammate) {
		const $thisTeammate = this.$projectUserTemplate.clone();
		$thisTeammate.removeAttr('id').removeClass('Template');
		$('.member-name', $thisTeammate).text(teammate.name);
		$('.member-icon > .inner-label', $thisTeammate).text(this.truncateName(teammate.name));

		const $taskList = $('.member-assigned-tasks', $thisTeammate);
		if (teammate.assignedTasks) {
			for (const assignment of teammate.assignedTasks) {
				const $newTask = $("<li><strong></strong><span></span></li>");
				$('strong', $newTask).text(assignment.taskNumber);
				$('span', $newTask).text(assignment.name);
				$newTask.appendTo($taskList);
			}
		} else {
			$taskList.html("<li><em>No tasks assigned</em></li>");
		}
		$thisTeammate.insertBefore($('.addUserCard', this.$projectTeamList));
	}
	fetchProject() {
		$.get('/projects/'+window.projectId).done( (project) => {
			console.log(project);
			this.renderProject(project);
		}).fail( (err) => {
			console.error("Failed to retrieve project", err);
			this.$navProjTitle.text("404: Project not found!");
		});
	}
	registerHandlers() {
		this.$addUserName.on('input', () => {
			const val = this.$addUserName.val();
			$('.addUserCard .member-icon').html(`<div class="inner-label">${this.truncateName(val)}</div>`);
		});
		this.$addUserBtn.click(() => {
			if (this.$addUserName[0].reportValidity && !this.$addUserName[0].reportValidity()) return;
			this.$addUserBtn.prop('disabled', true);
			const newUserName = this.$addUserName.val().trim();
			console.log("will add user", newUserName);
			$.post('/teammates', JSON.stringify({
				name: newUserName,
				projectId: window.projectId
			})).done((resp) => {
				console.log("Created new teammate", resp);
				this.renderTeammate(resp);
				this.$addUserName.val("");
				$('.addUserCard .member-icon').html(``);
				this.$addUserName.removeClass('is-invalid');
				this.$addUserError.text("");
				this.$addUserBtn.prop('disabled', false);
			}).fail((err) => {
				console.log("Failed to create new teammate", err);
				this.$addUserName.addClass('is-invalid');
				this.$addUserError.text(err.responseJSON?.message ?? "An unknown error occurred. Please try again.");
				this.$addUserBtn.prop('disabled', false);
			});
			
		});
	}
}

export {
	TeamView
};
